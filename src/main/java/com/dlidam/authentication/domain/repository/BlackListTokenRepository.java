package com.dlidam.authentication.domain.repository;

import com.dlidam.authentication.domain.BlackListToken;
import com.dlidam.authentication.domain.TokenType;

import java.util.List;

public interface BlackListTokenRepository {

    BlackListToken save(final BlackListToken blackListToken);

    List<BlackListToken> saveAll(final List<BlackListToken> blackListTokens);

    boolean existsByTokenTypeAndToken(final TokenType tokenType, final String accessToken);
}
