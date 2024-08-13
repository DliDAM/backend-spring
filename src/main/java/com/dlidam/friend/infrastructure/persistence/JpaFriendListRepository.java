package com.dlidam.friend.infrastructure.persistence;

import com.dlidam.friend.domain.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFriendListRepository extends JpaRepository<FriendList, Long> {
    Boolean existsByUserIdAndFriendId(final Long userId, final String customId);
}
