package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
