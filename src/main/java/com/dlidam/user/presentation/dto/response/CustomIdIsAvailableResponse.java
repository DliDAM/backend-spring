package com.dlidam.user.presentation.dto.response;

import com.dlidam.user.application.dto.CustomIdIsAvailableDto;

public record CustomIdIsAvailableResponse(boolean isAvailable) {
    public static CustomIdIsAvailableResponse from(final CustomIdIsAvailableDto customIdIsAvailableDto) {
        return new CustomIdIsAvailableResponse(
                customIdIsAvailableDto.isAvailable()
        );
    }
}
