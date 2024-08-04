package com.dlidam.authentication.infrastructure.oauth2.kakao;

import com.dlidam.authentication.configuration.KakaoProvidersConfigurationProperties;
import com.dlidam.authentication.domain.dto.UserInformationDto;
import com.dlidam.authentication.domain.exception.InvalidTokenException;
import com.dlidam.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoUserInformationProvider implements OAuth2UserInformationProvider {

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String KAKAO_ADMIN_TOKEN_TYPE = "KakaoAK ";
    private static final String REST_TEMPLATE_MESSAGE_SEPARATOR = ":";
    private static final int MESSAGE_INDEX = 0;

    private final RestTemplate restTemplate;

    private final KakaoProvidersConfigurationProperties providersConfigurationProperties;

    @Override
    public Oauth2Type supportsOauth2Type() {
        return Oauth2Type.KAKAO;
    }

    // 클라이언트에서 보낸 accessToken을 바탕으로 카카오 인증 서버에서 인증하는 로직
    @Override
    public UserInformationDto findUserInformation(String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + accessToken);

        final HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        try {
            final ResponseEntity<UserInformationDto> response = restTemplate.exchange(
                    providersConfigurationProperties.userInfoUri(),
                    HttpMethod.GET,
                    request,
                    UserInformationDto.class
            );

            return response.getBody();
        } catch (final HttpClientErrorException ex){
            final String message = ex.getMessage().split(REST_TEMPLATE_MESSAGE_SEPARATOR)[MESSAGE_INDEX];

            throw new InvalidTokenException(message, ex);
        }
    }

    // todo: 로그 아웃 / 탈퇴
    @Override
    public UserInformationDto unlinkUserBy(String oauthId) {
        return null;
    }
}
