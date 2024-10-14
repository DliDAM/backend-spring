package com.dlidam.authentication.presentation;

import com.dlidam.authentication.application.AuthenticationService;
import com.dlidam.authentication.application.BlackListTokenService;
import com.dlidam.authentication.application.dto.LoginInformationDto;
import com.dlidam.authentication.application.dto.TokenDto;
import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import com.dlidam.authentication.presentation.dto.request.LoginTokenRequest;
import com.dlidam.authentication.presentation.dto.request.LogoutRequest;
import com.dlidam.authentication.presentation.dto.request.RefreshTokenRequest;
import com.dlidam.authentication.presentation.dto.response.LoginInformationResponse;
import com.dlidam.authentication.presentation.dto.response.TokenResponse;
import com.dlidam.authentication.presentation.dto.response.ValidatedTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final BlackListTokenService blackListTokenService;

    @Operation(summary = "카카오 로그인")
    @PostMapping("/login/{oauth2Type}")
    public ResponseEntity<LoginInformationResponse> login(
            @PathVariable final Oauth2Type oauth2Type,
            @RequestBody final LoginTokenRequest request
    ){
        log.info("로그인 요청이 들어왔습니다.");
        final LoginInformationDto loginInformationDto =
                authenticationService.login(
                        oauth2Type,
//                        request.deviceToken(),
                        request.accessToken());

        return ResponseEntity.ok(LoginInformationResponse.from(loginInformationDto));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody final RefreshTokenRequest request){
        log.info("토큰 재발급 요청이 들어왔습니다.");
        final TokenDto tokenDto = authenticationService.refreshToken(request.refreshToken());

        return ResponseEntity.ok(TokenResponse.from(tokenDto));
    }

    @Operation(summary = "엑세스 토큰 유효성 검증")
    @GetMapping("/validate-token")
    public ResponseEntity<ValidatedTokenResponse> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken
    ){
        log.info("엑세스 토큰 유효성 검증 요청이 들어왔습니다.");
        final boolean validated = authenticationService.validateToken(accessToken);

        return ResponseEntity.ok(new ValidatedTokenResponse(validated));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
            @RequestBody @Valid final LogoutRequest request
    ) {
        log.info("로그아웃 요청이 들어왔습니다.");
        blackListTokenService.registerBlackListToken(accessToken, request.refreshToken());

        return ResponseEntity.noContent().build();
    }

}
