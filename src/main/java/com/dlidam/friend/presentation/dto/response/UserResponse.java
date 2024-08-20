package com.dlidam.friend.presentation.dto.response;

import com.dlidam.friend.application.dto.UserDto;

public record UserResponse(
        String customId,
        String name,
        String statusMessage,
        boolean isFriend
) {
    public static UserResponse from(final UserDto dto) {
        return new UserResponse(
                dto.customId(),
                dto.name(),
                dto.statusMessage(),
                dto.isFriend()
        );
    }
}
