package com.dlidam.user.domain.repository;

import com.dlidam.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    User save(final User user);

    Optional<User> findById(final Long id);

    Optional<User> findByOauthId(final String oauthId);

    boolean existsByIdAndDeletedIsTrue(final Long id);

    boolean existsByCustomIdEndingWith(final String id);

    boolean existsByCustomId(final String customId);

    Optional<User> findByCustomId(final String customId);
}
