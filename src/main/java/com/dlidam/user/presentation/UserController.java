package com.dlidam.user.presentation;

import com.dlidam.authentication.configuration.AuthenticateUser;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import com.dlidam.global.service.FastAPIService;
import com.dlidam.user.application.dto.*;
import com.dlidam.user.presentation.dto.request.CreateUserRequest;
import com.dlidam.user.application.UserService;
import com.dlidam.user.presentation.dto.request.CustomIdRequest;
import com.dlidam.user.presentation.dto.request.UpdateProfileRequest;
import com.dlidam.user.presentation.dto.response.CustomIdIsAvailableResponse;
import com.dlidam.user.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final FastAPIService fastAPIService;
    @Operation(summary = "회원 정보 추가 기입")
    @PostMapping("/my-info")
    public ResponseEntity<Void> createInfo(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateUserRequest createRequest
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(3L);
        log.info("userId = {}의 회원 정보 추가 기입 요청이 들어왔습니다.", userInfo.userId());
        userService.createInfo(CreateUserDto.of(userInfo.userId(), createRequest));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 정보 반환")
    @GetMapping("/my-info")
    public ResponseEntity<UserResponse> getMyInfo(
            @AuthenticateUser final AuthenticationUserInfo userInfo
    ) {
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 내 아이디 조회 요청이 들어왔습니다.", userInfo.userId());
        final UserInfoDto userInfoDto = userService.getMyInfo(userInfo.userId());
        final UserResponse userResponse = UserResponse.from(userInfoDto);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "회원 프로필 수정")
    @PostMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final UpdateProfileRequest updateProfileRequest
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 내 프로필 수정 요청이 들어왔습니다.", userInfo.userId());
        userService.updateProfile(UpdateProfileDto.of(userInfo.userId(), updateProfileRequest));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 아이디 중복 여부")
    @PostMapping("/validate")
    public ResponseEntity<CustomIdIsAvailableResponse> validateByCustomId(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CustomIdRequest customIdRequest
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(3L);
        log.info("userId = {}의 사용자 아이디 중복 조회 요청이 들어왔습니다.", userInfo.userId());
        final CustomIdIsAvailableDto customIdIsAvailableDto = userService.validateByCustomId(CustomIdDto.of(customIdRequest));
        final CustomIdIsAvailableResponse response = CustomIdIsAvailableResponse.from(customIdIsAvailableDto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "사용자 목소리 추가")
    @PostMapping("/voice")
    public ResponseEntity<String> addUserVoice(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestPart final MultipartFile voiceFile  // 음성 파일 인자로 받기
    ) {
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(3L);
        Long userId = userInfo.userId();
        log.info("userId = {}의 목소리 추가 요청이 들어왔습니다.", userInfo.userId());
        final String response = fastAPIService.sendToFastServer(voiceFile, userId);
        return ResponseEntity.ok(response);
    }

}
