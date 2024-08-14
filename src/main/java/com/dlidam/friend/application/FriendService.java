package com.dlidam.friend.application;

import com.dlidam.friend.application.dto.AddFriendDto;
import com.dlidam.friend.application.dto.FriendDto;
import com.dlidam.friend.application.dto.UserDto;
import com.dlidam.friend.application.exception.AlreadyFriendException;
import com.dlidam.friend.application.exception.FriendNotFoundException;
import com.dlidam.friend.application.exception.MemberNotFoundException;
import com.dlidam.friend.domain.FriendList;
import com.dlidam.friend.domain.repository.FriendListRepository;
import com.dlidam.friend.presentation.dto.request.UserSearchCondition;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendListRepository friendListRepository;

    public UserDto getFriend(final UserSearchCondition userSearchCondition) {
        final User friend = userRepository.findByCustomId(userSearchCondition.customId())
                .orElseThrow(() -> new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        return UserDto.of(friend);
    }

    @Transactional
    public void addFriend(final AddFriendDto friendDto) {
        final User user = userRepository.findById(friendDto.userId())
                .orElseThrow(() -> new MemberNotFoundException("지정한 사용자를 찾을 수 없습니다."));
        final User friend = userRepository.findByCustomId(friendDto.customId())
                .orElseThrow(() -> new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));
        final Boolean isExist = friendListRepository.existsByUserIdAndFriendId(friendDto.userId(), friendDto.customId());
        if(isExist){
            throw new AlreadyFriendException("이미 친구인 사용자 입니다.");
        }

        final FriendList newFriend = FriendList.builder()
                .user(user)
                .friendId(friend.getCustomId())
                .build();

        friendListRepository.save(newFriend);
    }

    public List<FriendDto> getFriends(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException("지정한 사용자를 찾을 수 없습니다."));
        final List<FriendList> friendLists = friendListRepository.findAllByUser(user);

        return friendLists.stream()
                .map(friendList -> {
                    final User friend = userRepository.findByCustomId(friendList.getFriendId())
                            .orElseThrow(() -> new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));
                    return FriendDto.from(friendList, friend);
                })
                .collect(Collectors.toList());
    }
}
