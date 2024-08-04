package com.dlidam.user.domain;

import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode  // Java 클래스에 대해 equals와 hashCode 메서드를 자동으로 생성
@ToString
public class OauthInformation {

    private String oauthId;

    @Column(name = "oauth2_type")
    @Enumerated(EnumType.STRING)
    private Oauth2Type oauth2Type;

    public OauthInformation(final String oauthId, final Oauth2Type oauth2Type){
        this.oauthId = oauthId;
        this.oauth2Type = oauth2Type;
    }
}
