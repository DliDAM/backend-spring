package com.dlidam.friend.application;

import com.dlidam.friend.application.dto.*;
import com.dlidam.friend.application.exception.FriendNotFoundException;
import com.dlidam.friend.application.exception.MemberNotFoundException;
import com.dlidam.friend.domain.FriendList;
import com.dlidam.friend.domain.FriendType;
import com.dlidam.friend.domain.repository.FriendListRepository;
import com.dlidam.friend.presentation.dto.request.FriendIdRequest;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.dlidam.friend.domain.FriendType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendListRepository friendListRepository;

    public UserDto getFriend(final FriendOperationDto friendDto) {
        final User friend = userRepository.findByCustomId(friendDto.customId())
                .orElseThrow(() -> new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        final Boolean isFriend = friendListRepository.existsByUserIdAndFriendId(friendDto.userId(), friendDto.customId());

        return UserDto.of(friend, isFriend);
    }

    @Transactional
    public AddSuccessDto addFriend(final FriendOperationDto friendDto) {
        final User user = userRepository.findById(friendDto.userId())
                .orElseThrow(() -> new MemberNotFoundException("지정한 사용자를 찾을 수 없습니다."));
        final User friend = userRepository.findByCustomId(friendDto.customId())
                .orElseThrow(() -> new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));
        final Boolean isExist = friendListRepository.existsByUserIdAndFriendId(friendDto.userId(), friendDto.customId());
        if(isExist){
            return AddSuccessDto.of(false);
        }

        final FriendList newFriend = FriendList.builder()
                .user(user)
                .friendId(friend.getCustomId())
                .build();

        friendListRepository.save(newFriend);

        return AddSuccessDto.of(true);
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

    @Transactional
    public void updateFriend(final FriendOperationDto friendDto) {
        if(!userRepository.existsById(friendDto.userId())){
            throw new MemberNotFoundException("지정한 사용자를 찾을 수 없습니다.");
        }
        if(!userRepository.existsByCustomId(friendDto.customId())){
            throw new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다.");
        }

        final FriendList friendList = friendListRepository.findByUserIdAndFriendId(friendDto.userId(), friendDto.customId());

        if(friendList.getFriendType().equals(FRIEND)){
            friendList.updateFriendStatus(CLOSE_FRIEND);
        } else {
            friendList.updateFriendStatus(FRIEND);
        }

        friendListRepository.save(friendList);
    }

    @Transactional
    public void deleteFriend(final FriendOperationDto friendDto) {
        if(!userRepository.existsById(friendDto.userId())){
            throw new MemberNotFoundException("지정한 사용자를 찾을 수 없습니다.");
        }
        if(!userRepository.existsByCustomId(friendDto.customId())){
            throw new FriendNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다.");
        }
        friendListRepository.deleteByUserIdAndFriendId(friendDto.userId(), friendDto.customId());
    }


}
