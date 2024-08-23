package com.dlidam.chat.application;


import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.repository.ChatMessageRepository;
import com.dlidam.chat.dto.request.ChatMessageRequestDTO;
import com.dlidam.chat.dto.response.ChatMessageDTO;
import com.dlidam.chat.dto.response.ChatRoomWithLastMessageResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    public final ChatRoomService chatRoomService;
    public final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(final ChatMessageRequestDTO request, final String senderName) {
          final ChatRoom chatRoom = chatRoomService.findById(request.getChatRoomId());

          final ChatMessage newChatMessage = new ChatMessage(
                  request.getSenderId(),
                  senderName,
                  request.getMessage(),
                  chatRoom
          );

          return chatMessageRepository.save(newChatMessage);
    }

//    @Transactional
//    public void sortChatMessage(List<ChatRoomWithLastMessageResponseDTO> chatRoomSimpleDTOList) {
//
//            for(ChatRoomWithLastMessageResponseDTO chatRoomSimpleDTO : chatRoomSimpleDTOList){
//
//                Comparator<ChatMessageDTO> comparator = Comparator.comparing(ChatMessageDTO::getCreatedAt);
//                List<ChatMessageDTO> sortedMessages = chatRoomSimpleDTO.getChatMessageDTOList().stream()
//                        .sorted(comparator)
//                        .collect(Collectors.toList());
//                chatRoomSimpleDTO.setChatMessageDTOList(sortedMessages);
//            }
//    }
}
