package com.dlidam.chat.presentation.dto.response;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ChatRoomSimpleDTO {

        private Long chatRoomId;
        private Long userId;
        private String userName;
        private String partnerName;
        List<ChatMessageDTO> chatMessageDTOList;
        private String lastChatMessage;
        private LocalDateTime lastChatTime;
        private String pictureUrl;

        public static ChatRoomSimpleDTO toChatRoomSimpleDTO(ChatRoom chatRoom){
                LocalDateTime time = null;
                List<ChatMessage> chatMessageList = chatRoom.getChatMessage();
                if(!chatMessageList.isEmpty()){
                        ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
                        time = lastChatMessage.getCreatedTime();}

                return ChatRoomSimpleDTO.builder()
                        .chatRoomId(chatRoom.getId())
                        .userId(chatRoom.getSender().getId())
                        .userName(chatRoom.getSender().getName())
                        .partnerName(chatRoom.getReceiver().getName())
                        .chatMessageDTOList(ChatMessageDTO.toChatMessageListDTO(chatRoom.getChatMessage()))
                        .lastChatMessage(chatRoom.getLastChatMessage())
                        .lastChatTime(time)
                      //  .pictureUrl(chatRoom.getReceiver().getPictureURL())
                        .build();
        }

        public static List<ChatRoomSimpleDTO> toChatRoomSimpleListDTO(List<ChatRoom> chatRoomList){
                return chatRoomList.stream()
                        .map(chatRoom -> toChatRoomSimpleDTO(chatRoom))
                        .collect(Collectors.toList());
        }

        public void setChatMessageDTOList(List<ChatMessageDTO> sortedMessages){
                this.chatMessageDTOList = sortedMessages ;}

}
