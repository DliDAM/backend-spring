package com.dlidam.configuration.webrtc;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Spring 서버가 메시지들을 RN과 FastAPI 사이를 포워딩
 * */
@Slf4j
@Component
public class WebRTCProxy {

    @Value("${fast-api.endpoint}")
    private String fastApiEndpoint;

    // todo: FFmegConfig 의존성 주입

//    private final SocketIOServer server;
//    private final SocketIONamespace namespace;



}
