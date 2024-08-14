package com.dlidam.user.application;

import com.dlidam.user.application.dto.CreateUserDto;
import com.dlidam.user.application.dto.CustomIdDto;
import com.dlidam.user.application.dto.CustomIdIsAvailableDto;
import com.dlidam.user.application.exception.UserNotFoundException;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

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

    public CustomIdIsAvailableDto validateByCustomId(final CustomIdDto customIdDto) {

        boolean isAvailable = !userRepository.existsByCustomId(customIdDto.customId());

        return CustomIdIsAvailableDto.from(isAvailable);
    }
}
