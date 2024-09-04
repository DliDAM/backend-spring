package com.dlidam.configuration.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FFmpegConfig {

    @Value("${spring.config.activate.on-profile}")
    private String activeProfile;

    public String getFFmpegPath() {
        log.info("[FFmpegConfig]-[getFFmpegPath] {}", activeProfile);
        if("local".equals(activeProfile)) {
            return "src/main/resources/ffmpeg/ffmpeg";
        } else {
            return "ffmpeg";
        }
    }
}
