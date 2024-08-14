package com.dlidam.user.application.dto;

import com.dlidam.user.domain.VoiceType;
import com.dlidam.user.presentation.dto.request.CreateUserRequest;

public record CreateUserDto(
        Long userId,
        String customId,
        String name,
        String phoneNumber,
        Boolean isDisabled,
        VoiceType voiceType
) {

    public static CreateUserDto of(final Long userId, final CreateUserRequest updateUserRequest){
        return new CreateUserDto(
                userId,
                updateUserRequest.customId(),
                updateUserRequest.name(),
                updateUserRequest.phoneNumber(),
                updateUserRequest.isDisabled(),
                updateUserRequest.voiceType()
        );
    }

}
