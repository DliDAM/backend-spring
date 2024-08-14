package com.dlidam.authentication.infrastructure.jwt;

import com.dlidam.authentication.configuration.JwtConfigurationProperties;
import com.dlidam.authentication.domain.TokenDecoder;
import com.dlidam.authentication.domain.TokenType;
import com.dlidam.authentication.domain.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private static final String CLAIM_NAME = "userId";

    private static final int BEARER_END_INDEX = 7;

    private final JwtConfigurationProperties jwtConfigurationProperties;

    // 토큰 타입과 토큰 문자열을 사용하여 토큰을 파싱하고, 클레임을 추출
    @Override
    public Optional<PrivateClaims> decode(final TokenType tokenType, final String token) {
        validateBearerToken(token);

        return this.parse(tokenType, token)
                .map(this::convert);
    }

    private void validateBearerToken(final String token) {
        try {
            final String tokenType = token.substring(0, BEARER_END_INDEX);

            validateTokenType(tokenType);
        } catch (final StringIndexOutOfBoundsException | NullPointerException ex){
            throw new InvalidTokenException("Bearer 타입이 아니거나 유효한 토큰이 아닙니다.", ex);
        }
    }

    private void validateTokenType(final String tokenType) {
        if(!BEARER_TOKEN_PREFIX.equals(tokenType)){
            throw new InvalidTokenException("Bearer 타입이 아닙니다.");
        }
    }

    // 토큰 타입과 토큰 문자열을 사용하여 토큰을 파싱하고, 클레임을 추출
    private Optional<Claims> parse(final TokenType tokenType, final String token){
        final String key = jwtConfigurationProperties.findTokenKey(tokenType);

        // 키를 사용하여 토큰을 파싱하고 클레임을 추출
        try {
            return Optional.of(
                    Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(UTF_8)))
                            .build()
                            .parseClaimsJws(findPureToken(token))
                            .getBody()
            );
        } catch (final JwtException ignored){
            return Optional.empty();
        }
    }

    private String findPureToken(final String token) {
        return token.substring(BEARER_TOKEN_PREFIX.length());
    }

    private PrivateClaims convert(final Claims claims) {
        return new PrivateClaims(claims.get(CLAIM_NAME, Long.class));
    }
}
