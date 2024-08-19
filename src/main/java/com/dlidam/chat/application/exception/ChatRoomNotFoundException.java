package com.dlidam.chat.application.exception;

public class ChatRoomNotFoundException extends IllegalArgumentException {

    public ChatRoomNotFoundException(final String message) {
        super(message);
    }
}