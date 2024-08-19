package com.dlidam.chat.domain.dto;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomWithLastMessageDTO {

    private ChatRoom chatRoom;
    private ChatMessage chatMessage;

}
