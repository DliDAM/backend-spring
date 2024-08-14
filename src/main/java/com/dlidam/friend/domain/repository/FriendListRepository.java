package com.dlidam.friend.domain.repository;

import com.dlidam.friend.domain.FriendList;
import com.dlidam.user.domain.User;

import java.util.List;

public interface FriendListRepository {
    Boolean existsByUserIdAndFriendId(final Long userId, final String customId);

    FriendList save(final FriendList newFriend);

    List<FriendList> findAllByUser(final User user);
}
