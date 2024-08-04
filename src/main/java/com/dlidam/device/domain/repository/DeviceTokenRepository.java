package com.dlidam.device.domain.repository;

import com.dlidam.device.domain.DeviceToken;

import java.util.Optional;

public interface DeviceTokenRepository {

    Optional<DeviceToken> findByUserId(final Long userId);

    DeviceToken save(final DeviceToken deviceToken);
}
