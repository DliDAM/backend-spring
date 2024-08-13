package com.dlidam.user.application.dto;

public record CustomIdIsAvailableDto(boolean isAvailable) {
    public static CustomIdIsAvailableDto from(final boolean isAvailable) {
        return new CustomIdIsAvailableDto(isAvailable);
    }
}
