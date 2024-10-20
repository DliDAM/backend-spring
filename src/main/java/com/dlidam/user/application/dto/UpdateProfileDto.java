package com.dlidam.user.application.dto;

import com.dlidam.user.presentation.dto.request.UpdateProfileRequest;

public record UpdateProfileDto(
        Long userId,
        String name,
        String statusMessage
) {

    public static UpdateProfileDto of(final Long userId, final UpdateProfileRequest updateProfileRequest) {
        return new UpdateProfileDto(
                userId,
                updateProfileRequest.name(),
                updateProfileRequest.statusMessage()
        );
    }
}
