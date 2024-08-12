package com.dlidam.chat.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageRequestDTO {

    private Long chatRoomId;
    private String sender;
    private String message;


    public ChatMessageRequestDTO(Long chatRoomId, String sender, String message){
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
    }
}
