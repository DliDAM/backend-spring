package com.dlidam.device.infrastructure.persistence;

import com.dlidam.device.domain.DeviceToken;
import com.dlidam.device.domain.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceTokenRepositoryImpl implements DeviceTokenRepository {

    private final JpaDeviceTokenRepository jpaDeviceTokenRepository;

    @Override
    public Optional<DeviceToken> findByUserId(final Long userId) {
        return jpaDeviceTokenRepository.findByUserId(userId);
    }

    @Override
    public DeviceToken save(DeviceToken deviceToken) {
        return jpaDeviceTokenRepository.save(deviceToken);
    }
}
