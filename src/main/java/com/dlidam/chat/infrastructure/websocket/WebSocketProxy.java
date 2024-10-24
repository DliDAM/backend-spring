package com.dlidam.chat.infrastructure.websocket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dlidam.chat.application.ChatMessageService;
import com.dlidam.chat.dto.request.ChatMessageRequestDTO;
import com.dlidam.configuration.audio.AudioConverter;
import com.dlidam.configuration.websocket.ConfigUtil;
import com.dlidam.configuration.websocket.WebSocketUtil;
import com.dlidam.user.application.UserService;
import com.dlidam.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.*;
import java.net.URI;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
public class WebSocketProxy {

    private final String fastApiEndpoint;
    private final SocketIOServer server;
    private final SocketIONamespace namespace;

    private WebSocketUtil fastAPIWebSocket;
    private Timer timer;

    private final AudioConverter audioConverter;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @Autowired
    public WebSocketProxy(
            SocketIOServer server,
            ConfigUtil configUtil,
            ChatMessageService chatMessageService,
            UserService userService
    ) {
        this.server = server;
        this.fastApiEndpoint = configUtil.getFastApiEndpoint();
        this.namespace = server.addNamespace("/websocket");    // WebSocket을 연결을 위한 네임스페이스
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("textMessage", String.class, textMessageListener());    // "textMessage" 이벤트를 청취하고 메시지가 수신되면 textMessageListener 호출
        this.chatMessageService = chatMessageService;
        this.userService = userService;
        this.audioConverter = new AudioConverter();
    }

    private void connectFastAPI(Timer timer, SocketIOClient client){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(fastAPIWebSocket == null || fastAPIWebSocket.isClosed()) {
                        fastAPIWebSocket = new WebSocketUtil(
                                new URI(fastApiEndpoint),
                                new Draft_6455(),
                                client
                        );
                        fastAPIWebSocket.connectBlocking();
                    }
                } catch (Exception e) {
                    log.error("[WebSocketProxy]-[connectFastAPI] WebSocket Connection Failed");
                }
            }
        }, 0, 60);
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[WebSocketProxy]-[Socketio]-[{}] Connected to WebSocketProxy Socketio through '{}'",
                    client.getSessionId().toString(),
                    handshakeData.getUrl());

            String chatRoomId = client.getHandshakeData().getSingleUrlParam("chatRoomId");
            if(chatRoomId == null) {
                log.error("chatRoomId is null. Cannot join the room.");
                return;
            }
            log.info("String chatRoomId: {}", chatRoomId);
            client.joinRoom(chatRoomId);

            timer = new Timer();
            connectFastAPI(timer, client);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[WebSocketProxy]-[Socketio]-[{}] Disconnected from WebSocketProxy Socketio Module",
                    client.getSessionId().toString());

            if(timer != null) {
                timer.cancel();
                timer.purge();
            }

            if(fastAPIWebSocket != null) {
                fastAPIWebSocket.close();
            }
        };
    }


    // 클라이언트로부터 문자열 메시지 수신 시 처리
    private DataListener<String> textMessageListener() {
        return (client, messagePayload, ackSender) -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ChatMessageRequestDTO chatMessageRequestDTO = objectMapper.readValue(messagePayload, ChatMessageRequestDTO.class);

                User sender = userService.findUserByCustomId(chatMessageRequestDTO.getSenderId());
                chatMessageService.save(chatMessageRequestDTO, sender.getName());

                if(!sender.isDisabled()){   // 비장애인 사용자
                    namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                            .sendEvent("messageData", chatMessageRequestDTO);
                }
                else {      // 장애인 사용자
                    if (fastAPIWebSocket != null && fastAPIWebSocket.isOpen()) {
                        String jsonPayload = objectMapper.writeValueAsString(chatMessageRequestDTO);
                        fastAPIWebSocket.send(jsonPayload);

                        ByteArrayOutputStream audioDataBuffer = new ByteArrayOutputStream();

                        fastAPIWebSocket.onMessageCallback = audioData -> {
                            log.info("[WebSocketProxy]-[FastAPI] creating Audio Data");
                            audioDataBuffer.write((byte[]) audioData);

                            byte[] completeAudioData = audioDataBuffer.toByteArray();

                            try {
                                // 새로운 AudioConverter 사용
                                byte[] wavData = audioConverter.convertToWav(completeAudioData);

                                // 파일 저장
                                String fileName = "output.wav";
                                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                                    fos.write(wavData);
                                }
                                log.info("[WebSocketProxy]-[FastAPI] Saved WAV file: {}", fileName);

                                // 클라이언트로 전송
                                String base64Audio = Base64.getEncoder().encodeToString(wavData);
                                namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                                        .sendEvent("audioData", base64Audio);
                                log.info("[WebSocketProxy]-[Socketio] Sent WAV audio data to client: {}", client.getSessionId().toString());

                            } catch (Exception e) {
                                log.error("[WebSocketProxy]-[FastAPI] Error processing audio: ", e);
                            }
                            audioDataBuffer.reset();
                        };
                    } else {
                        log.error("[WebSocketProxy]-[Socketio] FastAPI WebSocket is not connected");
                    }
                }
            } catch (Exception ex) {
                log.error("[WebSocketProxy]-[Socketio] Exception while processing text message", ex);
            }
        };
    }
}
