package com.dlidam.user.application.dto;

import com.dlidam.user.domain.User;
import com.dlidam.user.domain.VoiceType;
import com.google.firebase.auth.UserInfo;

public record UserInfoDto(
        String customId,
        String name,
        String phoneNumber,
        boolean isDisabled,
        VoiceType voiceType
) {
    public static UserInfoDto of(final User user) {
        return new UserInfoDto(
                user.getCustomId(),
                user.getName(),
                user.getPhoneNumber(),
                user.isDisabled(),
                user.getVoiceType());
    }
}
