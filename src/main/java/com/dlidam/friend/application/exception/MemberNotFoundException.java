package com.dlidam.friend.application.exception;

public class MemberNotFoundException extends IllegalArgumentException{

    public MemberNotFoundException(final String message) { super(message); }
}
