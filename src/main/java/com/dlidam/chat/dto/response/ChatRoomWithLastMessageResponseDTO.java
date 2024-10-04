package com.dlidam.chat.dto.response;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.dto.ChatRoomWithLastMessageDTO;
import com.dlidam.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomWithLastMessageResponseDTO {

        private Long chatRoomId;
        private String partnerId;
        private String partnerName;
//        List<ChatMessageDTO> chatMessageDTOList;
        private String lastChatMessage;
        private LocalDateTime lastChatTime;

        public static ChatRoomWithLastMessageResponseDTO of(
                final User user,
                final ChatRoomWithLastMessageDTO chatRoomWithLastMessageDTO
        ) {
                final ChatRoom chatRoom = chatRoomWithLastMessageDTO.getChatRoom();
                final User partner = chatRoom.calculateChatPartnerOf(user);
                final ChatMessage lastMessage = chatRoomWithLastMessageDTO.getChatMessage();

                if(lastMessage != null) {
                        return new ChatRoomWithLastMessageResponseDTO(
                                chatRoom.getId(),
                                partner.getCustomId(),
                                partner.getName(),
                                lastMessage.getMessage(),
                                lastMessage.getCreatedTime()
                        );
                }

                return new ChatRoomWithLastMessageResponseDTO(
                        chatRoom.getId(),
                        partner.getCustomId(),
                        partner.getName(),
                        null,
                        null
                );
        }
}
