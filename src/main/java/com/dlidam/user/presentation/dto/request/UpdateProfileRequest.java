package com.dlidam.user.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateProfileRequest(
        @NotEmpty(message = "사용자 이름이 입력되지 않았습니다.")
        String name,

        @NotEmpty(message = "사용자 한 줄 소개 입력되지 않았습니다.")
        String statusMessage
) {
}
