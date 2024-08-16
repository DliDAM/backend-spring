package com.dlidam.configuration.socketio;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigUtil {

    @Value("${socket.io.server}")
    private String socketServer;

    @Value("${socket.io.port}")
    private String socketPort;

    @Value("${socket.io.host}")
    private String host;

    @Value("${socket.fast-api-endpoint}")
    private String fastApiEndpoint;
}

