package com.dlidam.friend.application.dto;

import com.dlidam.user.domain.User;

public record FriendDto(
        String customId,
        String name,
        String statusMessage
) {
    public static FriendDto of(final User friend) {
        return new FriendDto(
                friend.getCustomId(),
                friend.getName(),
                friend.getStatusMessage()
        );
    }
}
