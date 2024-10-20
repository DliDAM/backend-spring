package com.dlidam.user.domain;

import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import com.dlidam.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)    // id 필드만을 사용하여 객체의 동등성을 비교
@ToString(of = {"id", "name", "deleted", "oauthInformation"})
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String customId;      // 중복 불가능한 사용자 ID

    @Column(length = 10)
    private String name;

    private boolean isDisabled;

    private String phoneNumber;

    @Enumerated(value = STRING)
    private VoiceType voiceType;

    @Column(length = 50)
    private String statusMessage;   // 한 줄 소개

    @Embedded
    private OauthInformation oauthInformation;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Builder
    private User(
            final String customId,
            final String oauthId,
            final Oauth2Type oauth2Type
    ) {
        this.customId = customId;
        this.oauthInformation = new OauthInformation(oauthId, oauth2Type);
    }

    public void createInfo(
            final String customId,
            final String name,
            final String phoneNumber,
            final Boolean isDisabled,
            final VoiceType voiceType) {
        this.customId = customId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isDisabled = isDisabled;
        this.voiceType = voiceType;
    }

    public void updateProfile(
            final String name,
            final String statusMessage
    ) {
        this.name = name;
        this.statusMessage = statusMessage;
    }
}
