package com.dlidam.device.application;

import com.dlidam.device.application.dto.PersistDeviceTokenDto;
import com.dlidam.device.domain.DeviceToken;
import com.dlidam.device.domain.repository.DeviceTokenRepository;
import com.dlidam.user.application.exception.UserNotFoundException;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    private final UserRepository userRepository;

    @Transactional
    public void persist(final Long userId, final PersistDeviceTokenDto deviceTokenDto) {
        final String newDeviceToken = deviceTokenDto.deviceToken();
        if(newDeviceToken == null || newDeviceToken.isBlank()){
            return;
        }

        // DeviceToken 객체 불러오기/생성하기
        final DeviceToken deviceToken = deviceTokenRepository.findByUserId(userId)
                .orElseGet(() -> createDeviceToken(userId, newDeviceToken));

        if(deviceToken.isDifferentToken(newDeviceToken)){
            deviceToken.updateDeviceToken(newDeviceToken);
        }

    }

    // DeviceToken 객체 생성 및 저장
    private DeviceToken createDeviceToken(final Long userId, final String newDeviceToken) {

        // userId는 기본키의 Id를 나타냄
        final User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        final DeviceToken deviceToken = new DeviceToken(findUser, newDeviceToken);

        return deviceTokenRepository.save(deviceToken);
    }
}
