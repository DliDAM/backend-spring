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
    public Optional<User> findById(final Long id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByOauthId(final String oauthId) {
        return jpaUserRepository.findByOauthId(oauthId);
    }

    @Override
    public boolean existsByIdAndDeletedIsTrue(final Long id) {
        return jpaUserRepository.existsByIdAndDeletedIsTrue(id);
    }

    @Override
    public boolean existsByCustomIdEndingWith(final String id) {
        return jpaUserRepository.existsByCustomIdEndingWith(id);
    }

    @Override
    public boolean existsByCustomId(final String customId) {
        return jpaUserRepository.existsByCustomId(customId);
    }
}
