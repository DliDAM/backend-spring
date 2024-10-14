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

    @Query(
            "SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.sender.id = :userId AND cr.senderConnect = true) " +
            "OR (cr.receiver.id = :userId AND cr.receiverConnect = true)"
    )
    List<ChatRoom> findAllChatRoomsByUserId(@Param("userId") Long userId);
}
