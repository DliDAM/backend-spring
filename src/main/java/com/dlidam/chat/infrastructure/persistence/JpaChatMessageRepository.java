package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<List<ChatMessage>> findAllByChatRoom(final ChatRoom chatRoom);

    @Query(
            "SELECT cm FROM ChatMessage cm " +
            "WHERE cm.id = (SELECT max(c.id) FROM ChatMessage c " +
            "WHERE c.chatRoom.id = :chatRoomId)"
    )
    ChatMessage findLastMessageInChatRoom(@Param("chatRoomId") final Long chatRoomId);
}
