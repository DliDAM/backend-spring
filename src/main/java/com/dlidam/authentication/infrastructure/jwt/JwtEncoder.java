package com.dlidam.authentication.infrastructure.jwt;

import com.dlidam.authentication.configuration.JwtConfigurationProperties;
import com.dlidam.authentication.domain.TokenEncoder;
import com.dlidam.authentication.domain.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class JwtEncoder implements TokenEncoder {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    public String encode(
            final LocalDateTime publishTime,
            final TokenType tokenType,
            final Map<String, Object> privateClaims) {

        final Date targetDate = convertDate(publishTime);
        final String key = jwtConfigurationProperties.findTokenKey(tokenType);
        final Long expiredHours = jwtConfigurationProperties.findExpiredHours(tokenType);

        return TOKEN_PREFIX + Jwts.builder()
                .setIssuedAt(targetDate)
                .setExpiration(new Date(targetDate.getTime() + expiredHours * 60 * 60 * 1000L))
                .addClaims(privateClaims)
                .signWith(Keys.hmacShaKeyFor(key.getBytes(UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date convertDate(final LocalDateTime targetTime) {
        final Instant targetInstant = targetTime.atZone(ZoneId.of("Asia/Seoul"))
                .toInstant();

        return Date.from(targetInstant);
    }
}
