package com.dlidam.authentication.domain.dto;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationStore {

    // 각 스레드에 독립적인 "AuthenticationUserInfo" 객체 저장
    private final ThreadLocal<AuthenticationUserInfo> threadLocalAuthenticationStore
            = new ThreadLocal<>();

    public void set(final AuthenticationUserInfo userInfo){
        threadLocalAuthenticationStore.set(userInfo);
    }

    public AuthenticationUserInfo get(){
        return threadLocalAuthenticationStore.get();
    }

    public void remove(){
        threadLocalAuthenticationStore.remove();
    }

}
