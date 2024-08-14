package com.dlidam.user.presentation.dto.request;

import com.dlidam.user.domain.VoiceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotEmpty(message = "사용자 아이디가 입력되지 않았습니다.")
        String customId,

        @NotEmpty(message = "사용자 이름이 입력되지 않았습니다.")
        String name,

        @NotEmpty(message = "사용자 핸드폰 번호가 입력되지 않았습니다.")
        String phoneNumber,

        @NotNull(message = "사용자 청각 장애 여부가 입력되지 않았습니다.")
        Boolean isDisabled,

        @NotNull(message = "사용자 목소리 형태가 입력되지 않았습니다.")
        VoiceType voiceType
) {
}
