package com.dlidam.chat.domain.repository;

import com.dlidam.chat.domain.ChatMessage;

public interface ChatMessageRepository {

    ChatMessage save(ChatMessage chatMessage);
}
