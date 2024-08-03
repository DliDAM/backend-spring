package com.dlidam.member.domain;

import com.dlidam.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String memberId;

    @Column(nullable = false, length = 50)
    private String email;

    private boolean idDisabled;

    private String phoneNumber;

    @Enumerated(value = STRING)
    private VoiceType voiceType;

    @Enumerated(value = STRING)
    private CallType callType;

    // todo: 프로필 사진

    @Column(length = 50)
    private String statusMessage;

}
