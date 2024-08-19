package com.dlidam.chat.dto.response;

import com.dlidam.chat.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ChatMessageDTO {

        private String senderName;    // 보내는 사람 이름
        private String message;
        private LocalDateTime createdAt;

        public static ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage){
                return ChatMessageDTO.builder()
                        .senderName(chatMessage.getSenderName())
                        .message(chatMessage.getMessage())
                        .createdAt(chatMessage.getCreatedTime())
                        .build();
        }

        public static List<ChatMessageDTO> toChatMessageListDTO(List<ChatMessage> chatMessageList){
                return chatMessageList.stream()
                        .map(chatMessage -> toChatMessageDTO(chatMessage))
                        .collect(Collectors.toList());
        }
}
