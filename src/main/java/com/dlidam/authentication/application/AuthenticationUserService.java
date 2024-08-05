package com.dlidam.authentication.application;

import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationUserService {

    private final UserRepository userRepository;

    // 사용자 탈퇴여부 확인용
    public boolean isWithdrawal(final Long userId) {
        return userRepository.existsByIdAndDeletedIsTrue(userId);
    }
}
