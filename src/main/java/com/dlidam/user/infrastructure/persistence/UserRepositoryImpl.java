package com.dlidam.user.infrastructure.persistence;

import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(final User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) { return jpaUserRepository.findById(id); }

    @Override
    public Optional<User> findByOauthId(final String oauthId) {
        return jpaUserRepository.findByOauthId(oauthId);
    }

    @Override
    public boolean existsByCustomIdEndingWith(String id) {
        return jpaUserRepository.existsByCustomIdEndingWith(id);
    }
}
