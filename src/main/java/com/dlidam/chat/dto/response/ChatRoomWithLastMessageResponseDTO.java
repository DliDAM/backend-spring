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

//        public static ChatRoomWithLastMessageResponseDTO toChatRoomSimpleDTO(ChatRoom chatRoom){
//                LocalDateTime time = null;
//                List<ChatMessage> chatMessageList = chatRoom.getChatMessage();
//                if(!chatMessageList.isEmpty()){
//                        ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
//                        time = lastChatMessage.getCreatedTime();}
//
//                return ChatRoomWithLastMessageResponseDTO.builder()
//                        .chatRoomId(chatRoom.getId())
//                        .userId(chatRoom.getSender().getId())
//                        .userName(chatRoom.getSender().getName())
//                        .partnerName(chatRoom.getReceiver().getName())
//                        .chatMessageDTOList(ChatMessageDTO.toChatMessageListDTO(chatRoom.getChatMessage()))
//                        .lastChatMessage(chatRoom.getLastChatMessage())
//                        .lastChatTime(time)
//                        .pictureUrl(chatRoom.getReceiver().getPictureURL())
//                        .build();
//        }
//
//        public static List<ChatRoomWithLastMessageResponseDTO> toChatRoomSimpleListDTO(List<ChatRoom> chatRoomList){
//                return chatRoomList.stream()
//                        .map(chatRoom -> toChatRoomSimpleDTO(chatRoom))
//                        .collect(Collectors.toList());
//        }
//
//        public void setChatMessageDTOList(List<ChatMessageDTO> sortedMessages){
//                this.chatMessageDTOList = sortedMessages ;}

}
