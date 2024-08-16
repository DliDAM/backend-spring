package com.dlidam.user.configuration;

import com.dlidam.user.presentation.dto.request.CreateUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateUserRequestValidator implements ConstraintValidator<CreateUserRequestValid, CreateUserRequest> {
    @Override
    public void initialize(CreateUserRequestValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateUserRequest request, ConstraintValidatorContext context) {
        if (request.isDisabled()) {
            return request.voiceType() != null;
        } else {
            return request.voiceType() == null;
        }
    }
}
