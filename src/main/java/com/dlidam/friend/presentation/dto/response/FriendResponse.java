package com.dlidam.friend.presentation.dto.response;

import com.dlidam.friend.application.dto.FriendDto;
import com.dlidam.friend.domain.FriendType;

public record FriendResponse(
        String name,
        String statusMessage,
        FriendType friendType
) {
    public static FriendResponse from(final FriendDto dto) {
        return new FriendResponse(
                dto.name(),
                dto.statusMessage(),
                dto.friendType()
        );
    }
}
