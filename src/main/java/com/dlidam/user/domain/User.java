package com.dlidam.user.domain;

import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import com.dlidam.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.*;
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

    private String name;

    private boolean idDisabled;

    private String phoneNumber;

    @Enumerated(value = STRING)
    private VoiceType voiceType;

    @Enumerated(value = STRING)
    private CallType callType;

    // todo: 프로필 사진

    @Column(length = 50)
    private String statusMessage;

    @Embedded
    private OauthInformation oauthInformation;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Builder
    private User(
//            final String customId,
            final String oauthId,
            final Oauth2Type oauth2Type
    ) {
//        this.customId = customId;
        this.oauthInformation = new OauthInformation(oauthId, oauth2Type);
    }

    public void updateCustomId(final String customId){ this.customId = customId; }

    public void updateName(final String name){ this.name = name; }

}
