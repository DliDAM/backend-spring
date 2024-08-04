package com.dlidam.device.application.dto;

import com.dlidam.device.presentation.dto.request.UpdateDeviceTokenRequest;

public record PersistDeviceTokenDto(String deviceToken) {

    public static PersistDeviceTokenDto from(final UpdateDeviceTokenRequest updateDeviceTokenRequest){
        return new PersistDeviceTokenDto(updateDeviceTokenRequest.deviceToken());
    }

}