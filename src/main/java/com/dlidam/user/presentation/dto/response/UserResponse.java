package com.dlidam.user.presentation.dto.response;

import com.dlidam.user.application.dto.CustomIdDto;
import com.dlidam.user.application.dto.UserInfoDto;

public record UserResponse(
        String customId,
        String name,
        boolean isDisabled
) {
    public static UserResponse from(final UserInfoDto userInfoDto) {
        return new UserResponse(userInfoDto.customId(), userInfoDto.name(), userInfoDto.isDisabled());
    }
}
