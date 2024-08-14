package com.dlidam.chat.application;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.repository.ChatRoomRepository;
import com.dlidam.user.domain.User;
import com.dlidam.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoom findChatRoomBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId,receiverId);
    }

    @Transactional
    public ChatRoom createChatRoom(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow();
        User user2 = userRepository.findById(userId2).orElseThrow();

        ChatRoom chatRoom = new ChatRoom(user1, user2);
        chatRoom.setUser1ConnectTrue();
        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    @Transactional
    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow();
    }

//    @Transactional
//    public List<ChatRoom> findAllChatRoomListByUser(User user) {
//        return chatRoomRepository.findAllByUser(user);
//    }

    @Transactional
    public void setLastChatMessage(List<ChatRoom> chatRoomList) {

            for (ChatRoom chatRoom : chatRoomList) {
                List<ChatMessage> chatMessageList = chatRoom.getChatMessage();
                if (!chatMessageList.isEmpty()) {
                    ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
                    chatRoom.setLastChatMessage(lastChatMessage.getMessage());
                }
            }
    }
}
