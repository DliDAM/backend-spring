package com.dlidam.authentication.presentation.dto.response;

import com.dlidam.authentication.application.dto.LoginInformationDto;

public record LoginInformationResponse(String accessToken, String refreshToken, boolean isSignUpUser) {

    public static LoginInformationResponse from(final LoginInformationDto dto){
        return new LoginInformationResponse(
                dto.tokenDto().accessToken(),
                dto.tokenDto().refreshToken(),
                dto.isSignUpUser()
        );
    }
}
