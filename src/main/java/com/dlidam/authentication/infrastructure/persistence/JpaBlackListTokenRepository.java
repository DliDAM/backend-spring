package com.dlidam.authentication.infrastructure.persistence;

import com.dlidam.authentication.domain.BlackListToken;
import com.dlidam.authentication.domain.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    boolean existsByTokenTypeAndToken(final TokenType tokenType, final String accessToken);
}
