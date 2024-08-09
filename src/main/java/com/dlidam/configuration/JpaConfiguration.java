package com.dlidam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
    // 데이터베이스 엔터티에 대한 감사(auditing) 기능을 활성화
}
