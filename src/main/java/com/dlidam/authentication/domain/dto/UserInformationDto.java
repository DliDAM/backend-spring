package com.dlidam.authentication.domain.dto;

public record UserInformationDto(Long id) {

    public String findUserId(){ return String.valueOf(id); }
}
