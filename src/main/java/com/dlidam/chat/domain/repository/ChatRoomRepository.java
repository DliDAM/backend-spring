package com.dlidam.chat.domain.repository;

import com.dlidam.chat.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(Long chatRoomId);

    // List<ChatRoom> findAllByUser(User user);
}
