package com.dlidam.user.application.dto;

import com.dlidam.user.presentation.dto.request.CustomIdRequest;

public record CustomIdDto(String customId) {
    public static CustomIdDto of(final CustomIdRequest customIdRequest) {
        return new CustomIdDto(customIdRequest.customId());
    }
}
