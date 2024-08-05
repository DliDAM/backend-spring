package com.dlidam.authentication.domain;

import com.dlidam.authentication.domain.exception.EmptyTokenException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "tokenType", "token"})
public class BlackListToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(length = 200, nullable = false)
    private String token;

    public BlackListToken(final TokenType tokenType, final String token){
        validateToken(token);

        this.tokenType = tokenType;
        this.token = token;
    }

    private void validateToken(final String targetToken) {
        if(targetToken == null || targetToken.isBlank()){
            throw new EmptyTokenException("비어있는 토큰입니다.");
        }
    }
}
