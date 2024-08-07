package com.dlidam.authentication.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(
        @NotEmpty(message = "refreshToken을 입력해주세요")
        String refreshToken
) {
}
