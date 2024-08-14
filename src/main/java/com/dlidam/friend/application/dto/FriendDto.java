package com.dlidam.friend.application.dto;

import com.dlidam.friend.domain.FriendList;
import com.dlidam.friend.domain.FriendType;
import com.dlidam.user.domain.User;

public record FriendDto(
        String customId,
        String name,
        String statusMessage,
        FriendType friendType
) {
    public static FriendDto from(final FriendList friendList, final User friend) {
        return new FriendDto(
                friend.getCustomId(),
                friend.getName(),
                friend.getStatusMessage(),
                friendList.getFriendType()
        );
    }
}
