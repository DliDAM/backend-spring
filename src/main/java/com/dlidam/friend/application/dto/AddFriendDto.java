package com.dlidam.friend.application.dto;

import com.dlidam.friend.presentation.dto.request.FriendAddRequest;

public record AddFriendDto(
        String customId,     // 추가하려는 친구 Id
        Long userId
) {
    public static AddFriendDto of(final FriendAddRequest request, final Long userId) {
        return new AddFriendDto(request.customId(), userId);
    }
}
