package com.dlidam.friend.application.dto;

import com.dlidam.friend.presentation.dto.request.FriendIdRequest;

public record FriendOperationDto(
        String customId,     // 친구 Id
        Long userId
) {
    public static FriendOperationDto of(final FriendIdRequest request, final Long userId) {
        return new FriendOperationDto(request.customId(), userId);
    }
}
