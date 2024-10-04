package com.dlidam.chat.infrastructure.webrtc;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dlidam.chat.application.ChatMessageService;
import com.dlidam.chat.dto.request.ChatMessageRequestDTO;
import com.dlidam.configuration.websocket.ConfigUtil;
import com.dlidam.configuration.websocket.WebSocketUtil;
import com.dlidam.user.application.UserService;
import com.dlidam.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
public class WebSocketProxy {

    private final String fastApiEndpoint;
    private final SocketIONamespace namespace;
    private WebSocketUtil fastAPIWebSocket;
    private Timer timer;

    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @Autowired
    public WebSocketProxy(SocketIOServer server, ConfigUtil configUtil, ChatMessageService chatMessageService,
                          UserService userService) {
        this.fastApiEndpoint = configUtil.getFastApiEndpoint();
        // WebSocket을 연곃을 위한 네임스페이스
        this.namespace = server.addNamespace("/websocket");
        // 클라이언트가 연결될 때 호출
        this.namespace.addConnectListener(onConnected());
        // 클라이언트가 연결을 종료할 때 호출
        this.namespace.addDisconnectListener(onDisconnected());
        // "textMessage" 이벤트를 청취하고 메시지가 수신되면 textMessageListener 호출
        this.namespace.addEventListener("textMessage", String.class, textMessageListener());
        this.chatMessageService = chatMessageService;
        this.userService = userService;
    }

//     FastAPI 서버와 WebSocket 연결 설정
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
                    log.error("[WebRTCProxy]-[connectFastAPI] WebSocket Connection Failed");
                }
            }
        }, 0, 60);
    }

    private ConnectListener onConnected() {
        return client -> {
            log.info("[WebRTCProxy]-[Socketio]-[{}] Connected to WebRTCProxy Socketio", client.getSessionId().toString());

            // 클라이언트가 연결 시에 데이터베이스의 채팅방 ID로 WebSocket 룸에 참가
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

    // 클라이언트 연결 종료 시 처리
    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[WebRTCProxy]-[Socketio]-[{}] Disconnected from WebRTCProxy Socketio", client.getSessionId().toString());
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
                log.info("========1=========");
                chatMessageService.save(chatMessageRequestDTO, sender.getName());
                log.info("========2=========");
                if(!sender.isDisabled()){   // 비장애인 사용자
                    namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                            .sendEvent("messageData", chatMessageRequestDTO);
                }
                else {      // 청각 장애인 사용자
                    if (fastAPIWebSocket != null && fastAPIWebSocket.isOpen()) {
                        // FastAPI 서버에 문자열 메시지 전송
                        fastAPIWebSocket.send(chatMessageRequestDTO.getMessage());

                        log.info("========3=========");

                        // FastAPI 서버로부터 응답 대기 및 오디오 데이터 수신
                        fastAPIWebSocket.onMessageCallback = audioData -> {
                            // 클라이언트로 오디오 데이터 전송
                            namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                                    .sendEvent("audioData", Base64.getEncoder().encodeToString(audioData));

                            log.info("audioData = {}", audioData);

                            log.info("[WebRTCProxy]-[Socketio] Sent audio data to client: {}", client.getSessionId().toString());
                        };
                    } else {
                        log.error("[WebRTCProxy]-[Socketio] FastAPI WebSocket is not connected");
                    }
                }
            } catch (Exception ex) {
                log.error("[WebRTCProxy]-[Socketio] Exception while processing text message", ex);
            }
        };
    }
}
