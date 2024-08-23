package com.dlidam.chat.dto.response;

import com.dlidam.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomResponseDTO {

    private Long chatRoomId;
    private String customId;
    private String senderName;
    private String receiverName;
    private List<ChatMessageDTO> messageDTOS;
    private Boolean senderConnect;
    private Boolean receiverConnect;

    public static ChatRoomResponseDTO senderRoomResponseDTO(final ChatRoom chatRoom, final List<ChatMessageDTO> messageDTOS){


        return new ChatRoomResponseDTO(
                chatRoom.getId(),
                chatRoom.getSender().getCustomId(),
                chatRoom.getSender().getName(),
                chatRoom.getReceiver().getName(),
                messageDTOS,
                chatRoom.getSenderConnect(),
                chatRoom.getReceiverConnect()
        );
    }

    public static ChatRoomResponseDTO receiverRoomResponseDTO(final ChatRoom chatRoom, final List<ChatMessageDTO> messageDTOS){

        return new ChatRoomResponseDTO(
                chatRoom.getId(),
                chatRoom.getReceiver().getCustomId(),
                chatRoom.getReceiver().getName(),
                chatRoom.getSender().getName(),
                messageDTOS,
                chatRoom.getSenderConnect(),
                chatRoom.getReceiverConnect()
        );
    }

}
