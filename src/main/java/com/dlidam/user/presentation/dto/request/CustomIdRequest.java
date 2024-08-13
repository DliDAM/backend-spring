package com.dlidam.user.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CustomIdRequest(
        @NotEmpty(message = "사용자 아이디가 입력되지 않았습니다.")
        String customId
) {
}
