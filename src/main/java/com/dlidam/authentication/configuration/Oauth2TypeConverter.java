package com.dlidam.authentication.configuration;

import com.dlidam.authentication.infrastructure.oauth2.Oauth2Type;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Oauth2TypeConverter implements Converter<String, Oauth2Type> {

    // 문자열을 Oauth2Type 열거형으로 변환
    @Override
    public Oauth2Type convert(final String typeName) {
        return Oauth2Type.from(typeName);
    }
}
