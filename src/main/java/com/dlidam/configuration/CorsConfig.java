package com.dlidam.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // CORS 설정에 ws://, wss:// 프로토콜 허용
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:8080", "https://localhost:3000",
                "https://localhost:8000", "http://3.34.121.34", "http://15.164.220.167",
                "ws://15.164.220.167", "wss://15.164.220.167", "ws://3.34.121.34", "wss//3.34.121.34"));

        // 모든 헤더 허용
        config.setAllowedHeaders(Arrays.asList("*"));

        // 허용된 HTTP 메서드
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 노출할 헤더
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Cookie", "Set-Cookie"));

        // 모든 경로에 대해 위의 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
