package com.dlidam.configuration.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dlidam.chat.application.ChatMessageService;
import com.dlidam.chat.presentation.dto.request.ChatMessageRequestDTO;
import com.dlidam.user.application.UserService;
import com.dlidam.user.domain.User;
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
public class WebsocketProxy {

    private final String fastApiEndpoint;
    private final SocketIONamespace namespace;
    private WebSocketUtil fastAPIWebSocket;
    private Timer timer;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @Autowired
    public WebsocketProxy(SocketIOServer server, ConfigUtil configUtil, ChatMessageService chatMessageService,
                          UserService userService) {
        this.fastApiEndpoint = configUtil.getFastApiEndpoint();
        this.namespace = server.addNamespace("/websocket");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("textMessage", ChatMessageRequestDTO.class, textMessageListener());
        this.chatMessageService = chatMessageService;
        this.userService = userService;
    }

    // WebSocket 연결 설정
    private void connectFastAPI(Timer timer, SocketIOClient client){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(fastAPIWebSocket == null || fastAPIWebSocket.isClosed()) {
                        fastAPIWebSocket = new WebSocketUtil(
                                new URI(fastApiEndpoint + "/ws"),
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

    // 클라이언트 연결 시 처리
    private ConnectListener onConnected() {
        return client -> {
            log.info("[WebRTCProxy]-[Socketio]-[{}] Connected to WebRTCProxy Socketio", client.getSessionId().toString());

            // 클라이언트가 연결 시에 데이터베이스의 채팅방 ID로 WebSocket 룸에 참가
            String chatRoomId = client.getHandshakeData().getSingleUrlParam("chatRoomId");
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
    private DataListener<ChatMessageRequestDTO> textMessageListener() {
        return (client, chatMessageRequestDTO, ackSender) -> {
            try {
                chatMessageService.save(chatMessageRequestDTO);
                User user = userService.findUSerById(chatMessageRequestDTO.getUserId());
                if(user.isDisabled() == false){
                    namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                            .sendEvent("messageData", chatMessageRequestDTO);
                }
                else {
                    if (fastAPIWebSocket != null && fastAPIWebSocket.isOpen()) {
                        // FastAPI 서버에 문자열 메시지 전송
                        fastAPIWebSocket.send(chatMessageRequestDTO.getMessage());

                        // FastAPI 서버로부터 응답 대기 및 오디오 데이터 수신
                        fastAPIWebSocket.onMessageCallback = audioData -> {
                            // 클라이언트로 오디오 데이터 전송
                            namespace.getRoomOperations(chatMessageRequestDTO.getChatRoomId().toString())
                                    .sendEvent("audioData", Base64.getEncoder().encodeToString(audioData));
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
