package com.dlidam.friend.application.dto;

import com.dlidam.user.domain.User;

public record UserDto(
        String customId,
        String name,
        String statusMessage
) {
    public static UserDto of(final User friend) {
        return new UserDto(
                friend.getCustomId(),
                friend.getName(),
                friend.getStatusMessage()
        );
    }
}
