package com.dlidam.authentication.presentation;

import com.dlidam.authentication.application.AuthenticationService;
import com.dlidam.authentication.application.dto.LoginInformationDto;
import com.dlidam.authentication.application.dto.TokenDto;
import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import com.dlidam.authentication.presentation.dto.request.LoginTokenRequest;
import com.dlidam.authentication.presentation.dto.request.LogoutRequest;
import com.dlidam.authentication.presentation.dto.request.RefreshTokenRequest;
import com.dlidam.authentication.presentation.dto.response.LoginInformationResponse;
import com.dlidam.authentication.presentation.dto.response.TokenResponse;
import com.dlidam.authentication.presentation.dto.response.ValidatedTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login/{oauth2Type}")
    public ResponseEntity<LoginInformationResponse> login(
            @PathVariable final Oauth2Type oauth2Type,
            @RequestBody final LoginTokenRequest request
    ){
        final LoginInformationDto loginInformationDto =
                authenticationService.login(
                        oauth2Type,
                        request.accessToken(),
                        request.deviceToken());

        return ResponseEntity.ok(LoginInformationResponse.from(loginInformationDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody final RefreshTokenRequest request){
        final TokenDto tokenDto = authenticationService.refreshToken(request.refreshToken());

        return ResponseEntity.ok(TokenResponse.from(tokenDto));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ValidatedTokenResponse> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken
    ){
        final boolean validated = authenticationService.validateToken(accessToken);

        return ResponseEntity.ok(new ValidatedTokenResponse(validated));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(
//            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
//            @RequestBody @Valid final LogoutRequest request
//    ) {
//
//    }

}