package com.dlidam.configuration.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer(ConfigUtil configUtil) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        config.setHostname(configUtil.getHost());   // Socket.IO 서버의 호스트
        config.setPort(Integer.parseInt(configUtil.getSocketPort()));   // Socket.IO 서버가 사용할 포트를 설정
        config.setOrigin("*");  // 모든 도메인에서의 요청을 허용하도록 설정
        config.setAllowCustomRequests(true);    // 사용자 정의 요청을 허용
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);

        return new SocketIOServer(config);
    }
}