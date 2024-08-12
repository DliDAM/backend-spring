package com.dlidam.chat.presentation.dto.response;

import com.dlidam.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateChatRoomResponseDTO {

    private Long chatRoomId;
    private String senderName;
    private String partnerName;
    private String pictureUrl;
    private Boolean user1Connect;
    private Boolean user2Connect;

    public CreateChatRoomResponseDTO(ChatRoom chatRoom){
        this.chatRoomId = chatRoom.getId();
        this.senderName = chatRoom.getSender().getName();
        this.partnerName = chatRoom.getReceiver().getName();
        this.user1Connect = chatRoom.getUser1Connect();
        this.user2Connect = chatRoom.getUser2Connect();
        // this.pictureUrl = chatRoom.getReceiver().getPictureUrl;
    }

}
