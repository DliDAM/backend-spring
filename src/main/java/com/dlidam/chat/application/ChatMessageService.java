package com.dlidam.chat.application;


import com.dlidam.chat.application.exception.ChatRoomNotFoundException;
import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.repository.ChatMessageRepository;
import com.dlidam.chat.domain.repository.ChatRoomRepository;
import com.dlidam.chat.dto.request.ChatMessageRequestDTO;
import com.dlidam.chat.dto.response.ChatMessageDTO;
import com.dlidam.chat.dto.response.ChatRoomWithLastMessageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatMessage save(final ChatMessageRequestDTO request, final String senderName) {
          final ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                  .orElseThrow(() -> new ChatRoomNotFoundException("요청하는 ID에 해당하는 채팅방을 찾을 수 없습니다."));

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
