package com.dlidam.configuration.socketio;


import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dlidam.chat.presentation.dto.request.ChatMessageRequestDTO;
import com.dlidam.chat.presentation.dto.request.SocketMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettySocketIO {
    private final SocketIONamespace namespace;

    @Autowired
    public NettySocketIO(SocketIOServer server){
        this.namespace = server.addNamespace("/socketio");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("stt", SocketMsgDTO.class, onReceived());
    }

    private DataListener<SocketMsgDTO> onReceived() {
        return (client, data, ackSender) -> {
            log.info("[NettySocketio]-[onReceived]-[{}] Received message '{}'", client.getSessionId().toString(), data);
            namespace.getBroadcastOperations().sendEvent("stt", data);
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[NettySocketio]-[onConnected]-[{}] Connected to Socket Module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("[NettySocketio]-[onDisconnected]-[{}] Disconnected from Socket Module.", client.getSessionId().toString());
        };
    }
}