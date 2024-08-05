package com.dlidam.authentication.configuration;

import com.dlidam.authentication.configuration.exception.UserUnauthorizedException;
import com.dlidam.authentication.domain.dto.AuthenticationStore;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러 메서드의 매개변수에 사용자 인증 정보를 자동으로 주입
* */
@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationStore store;
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthenticationUserInfo.class) &&
                parameter.hasParameterAnnotation(AuthenticateUser.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception
    {
        final AuthenticationUserInfo userInfo = store.get();

        if(isInvalidUserPrincipal(userInfo, parameter)){
            throw new UserUnauthorizedException("로그인이 필요한 기능입니다.");
        }

        return userInfo;
    }

    private boolean isInvalidUserPrincipal(
            final AuthenticationUserInfo userInfo,
            final MethodParameter parameter) {
        return userInfo == null ||
                (userInfo.userId() == null && parameter.getParameterAnnotation(AuthenticateUser.class).required());
    }

}
