package com.dlidam.authentication.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginTokenRequest(

        @NotEmpty(message = "AccessToken을 입력해주세요.")
        String accessToken
//        String deviceToken
) {
}
