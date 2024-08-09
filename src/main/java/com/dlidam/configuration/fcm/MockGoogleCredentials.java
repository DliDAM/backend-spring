package com.dlidam.configuration.fcm;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 테스트나 로컬 환경에서 사용할 수 있는 가짜 인증 자격
 * */
@EqualsAndHashCode(callSuper = false)
public class MockGoogleCredentials extends GoogleCredentials {

    private final String tokenValue;

    private final long expiryTime;

    public MockGoogleCredentials(String tokenValue){
        this(tokenValue, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
    }

    public MockGoogleCredentials(String tokenValue, long expiryTime) {
        this.tokenValue = tokenValue;
        this.expiryTime = expiryTime;
    }

    @Override
    public AccessToken refreshAccessToken() throws IOException {
        return new AccessToken(tokenValue, new Date(expiryTime));
    }
}
