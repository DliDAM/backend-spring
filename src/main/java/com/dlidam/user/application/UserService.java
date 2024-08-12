package com.dlidam.user.application;

import com.dlidam.user.domain.User;
import com.dlidam.user.infrastructure.persistence.JpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaUserRepository userRepository;

    @Transactional
    public User findUSerById(Long userId){
        return userRepository.findById(userId).orElseThrow();
    }
}
