package com.dlidam.user.presentation.dto.response;

import com.dlidam.user.application.dto.CustomIdDto;
import com.dlidam.user.application.dto.UserInfoDto;
import com.dlidam.user.domain.VoiceType;

public record UserResponse(
        String customId,
        String name,
        String phoneNumber,
        boolean isDisabled,
        VoiceType voiceType
) {
    public static UserResponse from(final UserInfoDto userInfoDto) {
        return new UserResponse(
                userInfoDto.customId(),
                userInfoDto.name(),
                userInfoDto.phoneNumber(),
                userInfoDto.isDisabled(),
                userInfoDto.voiceType());
    }
}
