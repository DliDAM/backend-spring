package com.dlidam.authentication.infrastructure.oauth2;

import java.util.Locale;

public enum Oauth2Type {

    KAKAO;

    public static Oauth2Type from(final String typeName){
        try {
            return Oauth2Type.valueOf(typeName.toUpperCase(Locale.ENGLISH));
        } catch (final IllegalArgumentException ex){
            throw new UnsupportedOperationException("지원하는 소셜 로그인 기능이 아닙니다,", ex);
        }
    }

    public String calculateNickname(final String name){
        final String type = this.name().toLowerCase(Locale.ENGLISH);

        return type + name;
    }
}
