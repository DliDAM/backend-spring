package com.dlidam.authentication.infrastructure.persistence;

import com.dlidam.authentication.domain.BlackListToken;
import com.dlidam.authentication.domain.TokenType;
import com.dlidam.authentication.domain.repository.BlackListTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BlackListTokenRepositoryImpl implements BlackListTokenRepository {

    private final JpaBlackListTokenRepository jpaBlackListTokenRepository;

    @Override
    public BlackListToken save(BlackListToken blackListToken) {
        return jpaBlackListTokenRepository.save(blackListToken);
    }

    @Override
    public List<BlackListToken> saveAll(List<BlackListToken> blackListTokens) {
        return jpaBlackListTokenRepository.saveAll(blackListTokens);
    }

    @Override
    public boolean existsByTokenTypeAndToken(TokenType tokenType, String accessToken) {
        return jpaBlackListTokenRepository.existsByTokenTypeAndToken(tokenType, accessToken);
    }
}
