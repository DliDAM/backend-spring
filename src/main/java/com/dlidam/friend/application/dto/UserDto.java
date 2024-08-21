package com.dlidam.friend.application.dto;

import com.dlidam.user.domain.User;

public record UserDto(
        String customId,
        String name,
        String statusMessage,
        boolean isFriend
) {
    public static UserDto of(final User friend, boolean isFriend) {
        return new UserDto(
                friend.getCustomId(),
                friend.getName(),
                friend.getStatusMessage(),
                isFriend
        );
    }
}
