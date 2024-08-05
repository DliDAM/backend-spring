package com.dlidam.authentication.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Oauth2WebConfiguration implements WebMvcConfigurer {

    private final Oauth2TypeConverter oauth2TypeConverter;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        // 문자열을 Oauth2Type으로 변환하는 기능을 등록
        registry.addConverter(oauth2TypeConverter);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // 모든 요청 경로(/**)에 대해 인터셉터가 동작 (단, /oauth2/** 경로는 제외)
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/oauth2/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        // 컨트롤러 메서드의 매개변수로 AuthenticationUserInfo 객체를 주입할 수 있도록 설정
        resolvers.add(authenticationPrincipalArgumentResolver);
    }
}
