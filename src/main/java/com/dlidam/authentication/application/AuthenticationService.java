package com.dlidam.authentication.application;

import com.dlidam.authentication.application.dto.LoginInformationDto;
import com.dlidam.authentication.application.dto.LoginUserInformationDto;
import com.dlidam.authentication.application.dto.TokenDto;
import com.dlidam.authentication.domain.Oauth2UserInformationProviderComposite;
import com.dlidam.authentication.domain.TokenDecoder;
import com.dlidam.authentication.domain.TokenEncoder;
import com.dlidam.authentication.domain.TokenType;
import com.dlidam.authentication.domain.dto.UserInformationDto;
import com.dlidam.authentication.domain.exception.InvalidTokenException;
import com.dlidam.authentication.infrastructure.jwt.PrivateClaims;
import com.dlidam.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import com.dlidam.device.application.DeviceTokenService;
import com.dlidam.device.application.dto.PersistDeviceTokenDto;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private static final String PRIVATE_CLAIMS_KEYS = "userId";

    private final DeviceTokenService deviceTokenService;

    private final UserRepository userRepository;

    private final Oauth2UserInformationProviderComposite providerComposite;

    private final TokenEncoder tokenEncoder;

    private final TokenDecoder tokenDecoder;


    @Transactional
    public LoginInformationDto login(
            final Oauth2Type oauth2Type,
            final String oauth2AccessToken,
            final String deviceToken
    ) {
        log.info("OAuth2Provider로부터 사용자 정보를 가져오는 로직");
        final OAuth2UserInformationProvider provider = providerComposite.findProvider(oauth2Type);
        log.info("토큰으로 사용자 정보(oauthId) 요청 로직");
        final UserInformationDto userInformationDto = provider.findUserInformation(oauth2AccessToken);
        log.info("이미 가입한 회원인지 확인하고 사용자 정보(객체) 요청 로직");
        final LoginUserInformationDto loginUserInfo = findOrPersistUser(oauth2Type, userInformationDto);
        log.info("디바이스 토큰 저장 로직");
        updateOrPersistDeviceToken(deviceToken, loginUserInfo.user());
        log.info("리프레시, 액세스 토큰 생성 로직");
        return LoginInformationDto.of(convertTokenDto(loginUserInfo), loginUserInfo);
    }

    private void updateOrPersistDeviceToken(final String deviceToken, final User persistUser) {
        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(deviceToken);

        deviceTokenService.persist(persistUser.getId(), persistDeviceTokenDto);
    }

    private LoginUserInformationDto findOrPersistUser(
            final Oauth2Type oauth2Type,
            final UserInformationDto userInformationDto
    ) {
        final AtomicBoolean isSignUpUser = new AtomicBoolean(false);

        final User signInUser =  userRepository.findByOauthId(userInformationDto.findUserId())
                .orElseGet(() -> {
                    final User user = User.builder()
//                            .customId(oauth2Type.calculateNickname(
//                                calculateRandomNumber())
//                            )
                            .oauthId(userInformationDto.findUserId())
                            .oauth2Type(oauth2Type)
                            .build();

                    isSignUpUser.set(true);
                    return userRepository.save(user);
                });

        return new LoginUserInformationDto(signInUser, isSignUpUser.get());
    }

//    private String calculateRandomNumber(){
//        String id = RandomUserIdGenerator.generate();
//
//        while (isAlreadyExist(id)){
//            id = RandomCustomIdGenerator.generate();
//        }
//
//        return id;
//    }
//
//    private boolean isAlreadyExist(final String id) {
//        return userRepository.existsByCustomIdEndingWith(id);
//    }

    private TokenDto convertTokenDto(final LoginUserInformationDto signInUserInfo) {

        final User loginUser = signInUserInfo.user();

        final String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(PRIVATE_CLAIMS_KEYS, loginUser.getId())
        );

        final String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of(PRIVATE_CLAIMS_KEYS, loginUser.getId())
        );

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenDto refreshToken(final String refreshToken) {
        final PrivateClaims privateClaims = tokenDecoder.decode(TokenType.REFRESH, refreshToken)
                .orElseThrow(
                        () -> new InvalidTokenException("유효한 토큰이 아닙니다.")
                );

        final String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(PRIVATE_CLAIMS_KEYS, privateClaims.userId())
        );

        final String newRefreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of(PRIVATE_CLAIMS_KEYS, privateClaims.userId())
        );

        return new TokenDto(accessToken, newRefreshToken);
    }

    public boolean validateToken(final String accessToken) {
        return tokenDecoder.decode(TokenType.ACCESS, accessToken)
                .isPresent();
    }
}
