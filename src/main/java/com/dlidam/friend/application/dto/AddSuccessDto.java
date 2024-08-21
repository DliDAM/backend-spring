package com.dlidam.friend.application.dto;

public record AddSuccessDto(
        boolean isSuccess
) {
    public static AddSuccessDto of(boolean isSuccess) {
        return new AddSuccessDto(isSuccess);
    }
}
