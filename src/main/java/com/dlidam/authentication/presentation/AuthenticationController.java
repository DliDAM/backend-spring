package com.dlidam.authentication.presentation;

import com.dlidam.authentication.application.AuthenticationService;
import com.dlidam.authentication.application.dto.LoginInformationDto;
import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import com.dlidam.authentication.presentation.dto.request.LoginTokenRequest;
import com.dlidam.authentication.presentation.dto.response.LoginInformationResponse;
import lombok.RequiredArgsConstructor;
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
}
