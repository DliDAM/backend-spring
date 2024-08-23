package com.dlidam.user.presentation.dto.response;

import com.dlidam.user.application.dto.CustomIdDto;

public record UserResponse(
        String customId
) {
    public static UserResponse from(final CustomIdDto customIdDto) {
        return new UserResponse(customIdDto.customId());
    }
}
