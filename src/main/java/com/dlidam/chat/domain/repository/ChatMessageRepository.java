package com.dlidam.chat.domain.repository;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {

    ChatMessage save(ChatMessage chatMessage);

    Optional<List<ChatMessage>> findAllByChatRoom(final ChatRoom chatRoom);
}
