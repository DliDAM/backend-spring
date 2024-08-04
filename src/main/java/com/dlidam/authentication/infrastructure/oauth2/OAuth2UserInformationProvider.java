package com.dlidam.authentication.infrastructure.oauth2;

import com.dlidam.authentication.domain.dto.UserInformationDto;

public interface OAuth2UserInformationProvider {

    Oauth2Type supportsOauth2Type();

    UserInformationDto findUserInformation(final String accessToken);

    UserInformationDto unlinkUserBy(final String oauthId);
}
