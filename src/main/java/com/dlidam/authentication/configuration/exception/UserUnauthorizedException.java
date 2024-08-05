package com.dlidam.authentication.configuration.exception;

public class UserUnauthorizedException extends IllegalArgumentException{

    public UserUnauthorizedException(final String message){ super(message);}
}
