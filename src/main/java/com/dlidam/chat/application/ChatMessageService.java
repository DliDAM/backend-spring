package com.dlidam.chat.application;


import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.repository.ChatMessageRepository;
import com.dlidam.chat.presentation.dto.request.ChatMessageRequestDTO;
import com.dlidam.chat.presentation.dto.response.ChatMessageDTO;
import com.dlidam.chat.presentation.dto.response.ChatRoomSimpleDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    public final ChatMessageRepository chatMessageRepository;
    public ChatMessage save(Long userId, ChatMessageRequestDTO request, ChatRoom chatRoom) {
          ChatMessage chatMessage = ChatMessage.builder()
                .message(request.getMessage())
                .userId(userId)
                .chatRoom(chatRoom)
                .build();
          chatMessageRepository.save(chatMessage);
          return chatMessage;
    }

    @Transactional
    public void sortChatMessage(List<ChatRoomSimpleDTO> chatRoomSimpleDTOList) {

            for(ChatRoomSimpleDTO chatRoomSimpleDTO : chatRoomSimpleDTOList){

                Comparator<ChatMessageDTO> comparator = Comparator.comparing(ChatMessageDTO::getCreatedAt);
                List<ChatMessageDTO> sortedMessages = chatRoomSimpleDTO.getChatMessageDTOList().stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
                chatRoomSimpleDTO.setChatMessageDTOList(sortedMessages);
            }
    }
}
