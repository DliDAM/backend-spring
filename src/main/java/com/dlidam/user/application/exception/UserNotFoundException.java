package com.dlidam.user.application.exception;

public class UserNotFoundException extends IllegalArgumentException{

    public UserNotFoundException(final String message){ super(message);}
}
