package com.dlidam.user.application;

import com.dlidam.user.application.dto.*;
import com.dlidam.user.application.exception.UserNotFoundException;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import com.dlidam.user.presentation.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createInfo(final CreateUserDto createUserDto) {
        final User user = userRepository.findById(createUserDto.userId())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        user.createInfo(
                createUserDto.customId(),
                createUserDto.name(),
                createUserDto.phoneNumber(),
                createUserDto.isDisabled(),
                createUserDto.voiceType()
                );
        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(final UpdateProfileDto updateProfileDto) {
        final User user = userRepository.findById(updateProfileDto.userId())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        user.updateProfile(updateProfileDto.name(), updateProfileDto.statusMessage());
        userRepository.save(user);
    }

    public UserInfoDto getMyInfo(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        return UserInfoDto.of(user);
    }

    public CustomIdIsAvailableDto validateByCustomId(final CustomIdDto customIdDto) {
        boolean isAvailable = !userRepository.existsByCustomId(customIdDto.customId());
        return CustomIdIsAvailableDto.from(isAvailable);
    }

    public User findUserByCustomId(final String senderId) {
        return userRepository.findByCustomId(senderId)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
    }



}
