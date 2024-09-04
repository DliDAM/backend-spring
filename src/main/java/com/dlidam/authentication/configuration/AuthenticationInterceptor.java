package com.dlidam.authentication.configuration;

import com.dlidam.authentication.application.AuthenticationUserService;
import com.dlidam.authentication.application.BlackListTokenService;
import com.dlidam.authentication.domain.TokenDecoder;
import com.dlidam.authentication.domain.TokenType;
import com.dlidam.authentication.domain.dto.AuthenticationStore;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import com.dlidam.authentication.domain.exception.InvalidTokenException;
import com.dlidam.authentication.infrastructure.jwt.PrivateClaims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final BlackListTokenService blackListTokenService;

    private final AuthenticationUserService authenticationUserService;

    private final TokenDecoder tokenDecoder;

    private final AuthenticationStore store;

    // HTTP 요청이 컨트롤러에 도달하기 전에 실행되는 로직
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception
    {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//        log.info("액세스 토큰 = {} 디코드 하는 중 입니다.", accessToken);

        // 토큰이 없거나 비어있는 경우
        if(isNotRequiredAuthentication(accessToken)){
            store.set(new AuthenticationUserInfo(null));
            return true;
        }

        // 로그아웃된 토큰인지 확인
        validateLogoutToken(accessToken);

        final PrivateClaims privateClaims = tokenDecoder.decode(TokenType.ACCESS, accessToken)
                .orElseThrow(() -> new InvalidTokenException("유효한 토큰이 아닙니다."));

        if(authenticationUserService.isWithdrawal(privateClaims.userId())){
            throw new InvalidTokenException("유효한 토큰이 아닙니다.(탈퇴한 사용자 입니다)");
        }

        store.set(new AuthenticationUserInfo(privateClaims.userId()));
        log.info("액세스 토큰 디코드가 완료되었습니다. 사용자 ID = {}", privateClaims.userId());

        return true;
    }

    private boolean isNotRequiredAuthentication(final String token) {
        return token == null || token.isEmpty();
    }

    private void validateLogoutToken(final String accessToken) {
        if(blackListTokenService.existsBlackListToken(TokenType.ACCESS, accessToken)){
            throw new InvalidTokenException("유효한 토큰이 아닙니다.");
        }
    }

    // 요청 처리가 완료된 후에 실행
    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex) throws Exception {
        store.remove();
    }
}
