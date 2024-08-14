package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    Optional<ChatRoom> findById(Long chatRoomId);

//    List<ChatRoom> findAllByUser(User user);
}
