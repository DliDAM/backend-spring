package com.dlidam.user.infrastructure.persistence;

import com.dlidam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {


    @Query("""
        SELECT u
        FROM User u
        WHERE u.deleted = false AND u.oauthInformation.oauthId = :oauthId
    """)
    Optional<User> findByOauthId(final String oauthId);

    boolean existsByCustomIdEndingWith(final String id);

    boolean existsByIdAndDeletedIsTrue(final Long id);

    boolean existsByCustomId(final String customId);

    Optional<User> findByCustomId(final String customId);
}
