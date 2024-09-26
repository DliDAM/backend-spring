package com.dlidam.user.application.dto;

import com.dlidam.user.domain.User;
import com.google.firebase.auth.UserInfo;

public record UserInfoDto(
        String customId,
        String name,
        boolean isDisabled
) {
    public static UserInfoDto of(final User user) {
        return new UserInfoDto(user.getCustomId(), user.getName(), user.isDisabled());
    }
}
