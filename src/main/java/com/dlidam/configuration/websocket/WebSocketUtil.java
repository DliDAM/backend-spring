package com.dlidam.configuration.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

@Slf4j
public class WebSocketUtil extends WebSocketClient {

    private final SocketIOClient socketIOClient;
    public WebSocketUtil(URI serverUri, Draft protocolDraft, SocketIOClient socketIOClient) {
        super(serverUri, protocolDraft);
        this.socketIOClient = socketIOClient;
    }

    public interface OnMessageCallback {
        void onMessage(byte[] audioData);
    }

    public OnMessageCallback onMessageCallback;

    @Override
    public void onMessage(ByteBuffer bytes) {
        log.info("[WebSocketUtil]-[onMessage] Received audio data from FastAPI");

        // 콜백을 통해서 Spring 서버로 전송
        if (onMessageCallback != null) {
            onMessageCallback.onMessage(bytes.array());
        }
    }

    @Override
    public void onMessage(String message) {
        log.info("[WebSocketUtil]-[onMessage] Received message {}", message);
        // 메시지 수신 시 로그 출력
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("[WebSocketUtil] WebSocket connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[WebSocketUtil] WebSocket connection closed: {}", reason);
    }

    @Override
    public void onError(Exception ex) {
        log.error("[WebSocketUtil] WebSocket error occurred", ex);
    }
}
