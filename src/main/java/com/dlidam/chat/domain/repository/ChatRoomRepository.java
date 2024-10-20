package com.dlidam.chat.domain.repository;

import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.dto.ChatRoomWithLastMessageDTO;
import com.dlidam.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(Long chatRoomId);

    Optional<ChatRoom> findBySenderAndReceiver(final User sender, final User receiver);

    List<ChatRoom> findAllChatRoomsByUserId(final Long id);

    void deleteById(final Long chatRoomId);
}
