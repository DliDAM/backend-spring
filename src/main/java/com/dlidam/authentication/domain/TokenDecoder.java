package com.dlidam.authentication.domain;

import com.dlidam.authentication.infrastructure.jwt.PrivateClaims;

import java.util.Optional;

public interface TokenDecoder {

    Optional<PrivateClaims> decode(final TokenType tokenType, final String token);
}
