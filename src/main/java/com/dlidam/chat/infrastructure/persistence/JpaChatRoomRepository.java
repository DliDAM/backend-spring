package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.dto.ChatRoomWithLastMessageDTO;
import com.dlidam.chat.dto.response.ChatRoomWithLastMessageResponseDTO;
import com.dlidam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    Optional<ChatRoom> findById(final Long chatRoomId);

    Optional<ChatRoom> findBySenderAndReceiver(final User sender, final User receiver);

    @Query("SELECT new com.dlidam.chat.domain.dto.ChatRoomWithLastMessageDTO( " +
            "c, " +
            "cm " +
            ") " +
            "FROM ChatRoom c " +
            "LEFT JOIN c.chatMessage cm " +
            "ON cm.id = (SELECT MAX(cm2.id) FROM ChatMessage cm2 WHERE cm2.chatRoom.id = c.id) " +
            "JOIN User u " +
            "ON (c.sender.id = u.id OR c.receiver.id = u.id) " +
            "WHERE u.id = :userId " +
            "ORDER BY cm.createdTime DESC")
    List<ChatRoomWithLastMessageDTO> findAllChatRoomByUserIdOrderByLastMessage(@Param("userId") final Long userId);

}
