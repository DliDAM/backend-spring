package com.dlidam.chat.application;

import com.dlidam.chat.application.exception.ChatRoomNotFoundException;
import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.dto.ChatRoomWithLastMessageDTO;
import com.dlidam.chat.domain.repository.ChatMessageRepository;
import com.dlidam.chat.domain.repository.ChatRoomRepository;
import com.dlidam.chat.dto.response.ChatMessageDTO;
import com.dlidam.chat.dto.response.ChatRoomResponseDTO;
import com.dlidam.chat.dto.response.ChatRoomWithLastMessageResponseDTO;
import com.dlidam.user.application.UserService;
import com.dlidam.user.application.exception.UserNotFoundException;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoom getSenderChatRoom(final Long senderId, final String receiverId) {

        final User sender = userRepository.findById(senderId).
                orElseThrow(() -> new UserNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        final User receiver = userRepository.findByCustomId(receiverId)
                .orElseThrow(() -> new UserNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        final ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(sender, receiver)
                .orElseGet(() ->{
                    final ChatRoom newChatRoom = new ChatRoom(sender, receiver);
                    newChatRoom.setSenderConnectTrue();

                    return chatRoomRepository.save(newChatRoom);
                });

        return chatRoom;
    }

    public ChatRoomResponseDTO getSenderMessages(final ChatRoom chatRoom) {

        final List<ChatMessage> messages = chatMessageRepository.findAllByChatRoom(chatRoom)
                .orElseGet(ArrayList::new);

        final List<ChatMessageDTO> chatMessageDTOS = ChatMessageDTO.toChatMessageListDTO(messages);

        return ChatRoomResponseDTO.senderRoomResponseDTO(chatRoom,chatMessageDTOS);
    }

    @Transactional
    public ChatRoom getReceiverChatRoom(final Long receiverId, final String senderId) {

        final User receiver = userRepository.findById(receiverId).
                orElseThrow(() -> new UserNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        final User sender = userRepository.findByCustomId(senderId)
                .orElseThrow(() -> new UserNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        final ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new ChatRoomNotFoundException("요청하는 채팅방을 찾을 수 없습니다."));

        chatRoom.setReceiverConnectTrue();

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomResponseDTO getReceiverMessages(ChatRoom chatRoom) {
        final List<ChatMessage> messages = chatMessageRepository.findAllByChatRoom(chatRoom)
                .orElseGet(ArrayList::new);

        final List<ChatMessageDTO> chatMessageDTOS = ChatMessageDTO.toChatMessageListDTO(messages);

        return ChatRoomResponseDTO.receiverRoomResponseDTO(chatRoom,chatMessageDTOS);
    }

    public List<ChatRoomWithLastMessageResponseDTO> getAllChatRooms(final Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        final List<ChatRoomWithLastMessageDTO> chatRoomWithLastMessageDTOS =
                chatRoomRepository.findAllChatRoomByUserIdOrderByLastMessage(user.getId());

        return chatRoomWithLastMessageDTOS.stream()
                .map(dto -> ChatRoomWithLastMessageResponseDTO.of(user, dto))
                .toList();
    }

//    @Transactional
//    public void setLastChatMessage(List<ChatRoom> chatRoomList) {
//
//        for (ChatRoom chatRoom : chatRoomList) {
//            List<ChatMessage> chatMessageList = chatRoom.getChatMessage();
//            if (!chatMessageList.isEmpty()) {
//                ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
//                chatRoom.setLastChatMessage(lastChatMessage.getMessage());
//            }
//        }
//    }

    public ChatRoom findById(final Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("요청하는 ID에 해당하는 채팅방을 찾을 수 없습니다."));
    }

    public ChatRoomResponseDTO getChatMessages(Long chatRoomId, Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("요청하는 ID에 대한 사용자를 찾을 수 없습니다."));

        ChatRoom chatRoom = findById(chatRoomId);
        if (chatRoom.getSender().equals(user)){
            return getSenderMessages(chatRoom);
        } else if (chatRoom.getReceiver().equals(user)) {
            return getReceiverMessages(chatRoom);
        }
        throw new ChatRoomNotFoundException("해당 채팅방의 내역을 불러올 수 없습니다.");
    }
}
