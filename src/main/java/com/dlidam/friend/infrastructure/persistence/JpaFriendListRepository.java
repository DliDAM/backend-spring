package com.dlidam.friend.infrastructure.persistence;

import com.dlidam.friend.domain.FriendList;
import com.dlidam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFriendListRepository extends JpaRepository<FriendList, Long> {

    Boolean existsByUserIdAndFriendId(final Long userId, final String customId);

    List<FriendList> findAllByUser(final User user);

    void deleteByUserIdAndFriendId(final Long userId, final String customId);
}
