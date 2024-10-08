package com.dlidam.chat.infrastructure.webrtc;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dlidam.chat.application.ChatMessageService;
import com.dlidam.chat.dto.request.ChatMessageRequestDTO;
import com.dlidam.configuration.ffmpeg.AudioConverter;
import com.dlidam.configuration.ffmpeg.FFmpegConfig;
import com.dlidam.configuration.websocket.ConfigUtil;
import com.dlidam.configuration.websocket.WebSocketUtil;
import com.dlidam.user.application.UserService;
import com.dlidam.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
public class WebSocketProxy {

    @Autowired
    private FFmpegConfig ffmpegConfig;

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
        this.audioConverter = new AudioConverter(ffmpegConfig);
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
                    log.error("[WebRTCProxy]-[connectFastAPI] WebSocket Connection Failed");
                }
            }
        }, 0, 60);
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[WebRTCProxy]-[Socketio]-[{}] Connected to WebRTCProxy Socketio through '{}'",
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
            log.info("[WebRTCProxy]-[Socketio]-[{}] Disconnected from WebSocketProxy Socketio Module",
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
                else {      // 청각 장애인 사용자
                    if (fastAPIWebSocket != null && fastAPIWebSocket.isOpen()) {
                        String jsonPayload = objectMapper.writeValueAsString(chatMessageRequestDTO);
                        // FastAPI 서버에 문자열 메시지 전송
                        fastAPIWebSocket.send(jsonPayload);
                        // FastAPI 서버로부터 응답 대기 및 오디오 데이터 수신
                        fastAPIWebSocket.onMessageCallback = audioData -> {
                            // 클라이언트로 오디오 데이터 전송
                            namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                                    .sendEvent("audioData", Base64.getEncoder().encodeToString(audioData));

                            log.info("audioData = {}", audioData);

                            log.info("output.raw 파일로 저장");
                            String rawFilePath = "output.raw";
                            saveAudioToFile(rawFilePath, audioData);

                            log.info("output.raw를 output.wav로 변환");
                            String wavFilePath = "output.wav";
                            convertRawToWav(rawFilePath, wavFilePath, 22050, 1, 2);

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

    private void saveAudioToFile(String outputFile, byte[] audioData) {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(audioData);
            log.info("Audio saved to {}", outputFile);
        } catch (IOException e) {
            log.error("Error saving audio to file: {}", e.getMessage());
        }
    }

    private void convertRawToWav(String inputFile, String outputFile, float sampleRate, int channels, int sampleWidth) {
        try {
            // Create a new AudioInputStream
            AudioFormat format = new AudioFormat(sampleRate, sampleWidth * 8, channels, true, false);
            byte[] rawData = Files.readAllBytes(Paths.get(inputFile));

            try (AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(rawData), format, rawData.length / (sampleWidth * channels))) {

                // Write to the WAV file
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(outputFile));
                log.info("Converted {} to {}", inputFile, outputFile);
            }
        } catch (Exception e) {
            log.error("Error converting {} to {}: {}", inputFile, outputFile, e.getMessage());
        }
    }

}
