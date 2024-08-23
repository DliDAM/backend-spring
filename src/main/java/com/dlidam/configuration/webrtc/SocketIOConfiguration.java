package com.dlidam.configuration.webrtc;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SocketIOConfiguration {
    @Bean
    public SocketIOServer socketIOServer(ConfigUtil configUtil) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(Integer.parseInt(configUtil.getSocketPort()));
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);

        return new SocketIOServer(config);
    }

}