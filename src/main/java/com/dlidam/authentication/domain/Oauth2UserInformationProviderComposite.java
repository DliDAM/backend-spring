package com.dlidam.authentication.domain;

import com.dlidam.authentication.domain.exception.UnsupportedSocialLoginException;
import com.dlidam.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Oauth2UserInformationProviderComposite {

    private final Map<Oauth2Type, OAuth2UserInformationProvider> mappings;

    public Oauth2UserInformationProviderComposite(final Set<OAuth2UserInformationProvider> providers) {
        this.mappings = providers.stream()
                .collect(Collectors.toMap(
                        OAuth2UserInformationProvider::supportsOauth2Type,
                        provider -> provider
                ));
    }

    public OAuth2UserInformationProvider findProvider(final Oauth2Type oauth2Type){
        final OAuth2UserInformationProvider provider = mappings.get(oauth2Type);

        if(provider == null){
            throw new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다.");
        }

        return provider;
    }
}
