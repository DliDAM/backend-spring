package com.dlidam.user.presentation.dto.request;

import com.dlidam.user.configuration.CreateUserRequestValid;
import com.dlidam.user.domain.VoiceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@CreateUserRequestValid
public record UpdateUserRequest(

        @NotEmpty(message = "사용자 이름이 입력되지 않았습니다.")
        String name,

        @NotEmpty(message = "사용자 핸드폰 번호가 입력되지 않았습니다.")
        String phoneNumber,

        @NotNull(message = "사용자 청각 장애 여부가 입력되지 않았습니다.")
        Boolean isDisabled,

        VoiceType voiceType
) {
}
