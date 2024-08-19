package com.dlidam.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDTO {

    private Long chatRoomId;
    private String senderId;    // 보낸 사람 아이디
    private String senderName;  // 보낸 사람 이름
    private String message;

}
