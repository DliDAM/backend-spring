package com.dlidam.device.infrastructure.persistence;

import com.dlidam.device.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByUserId(final Long userId);
}
