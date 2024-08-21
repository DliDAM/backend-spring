package com.dlidam.friend.infrastructure.persistence;

import com.dlidam.friend.domain.FriendList;
import com.dlidam.friend.domain.repository.FriendListRepository;
import com.dlidam.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendListRepositoryImpl implements FriendListRepository {

    private final JpaFriendListRepository jpaFriendListRepository;

    @Override
    public Boolean existsByUserIdAndFriendId(final Long userId, final String customId) {
        return jpaFriendListRepository.existsByUserIdAndFriendId(userId, customId);
    }

    @Override
    public void deleteByUserIdAndFriendId(final Long userId, final String customId) {
        jpaFriendListRepository.deleteByUserIdAndFriendId(userId, customId);
    }

    @Override
    public FriendList save(final FriendList newFriend) {
        return jpaFriendListRepository.save(newFriend);
    }

    @Override
    public List<FriendList> findAllByUser(final User user) {
        return jpaFriendListRepository.findAllByUser(user);
    }
}
