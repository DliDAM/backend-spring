package com.dlidam.friend.application.exception;

public class FriendNotFoundException extends IllegalArgumentException {

    public FriendNotFoundException(final String message) { super(message); }
}
