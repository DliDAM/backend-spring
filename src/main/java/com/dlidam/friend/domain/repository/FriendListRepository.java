package com.dlidam.friend.domain.repository;

import com.dlidam.friend.domain.FriendList;

public interface FriendListRepository {
    Boolean existsByUserIdAndFriendId(final Long userId, final String customId);

    FriendList save(final FriendList newFriend);
}
