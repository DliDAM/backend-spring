package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    private final JpaChatMessageRepository jpaChatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage){return jpaChatMessageRepository.save(chatMessage);}
}
