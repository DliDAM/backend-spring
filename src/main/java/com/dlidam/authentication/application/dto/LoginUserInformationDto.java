package com.dlidam.authentication.application.dto;

import com.dlidam.user.domain.User;

public record LoginUserInformationDto(User user, boolean persisted) {
}
