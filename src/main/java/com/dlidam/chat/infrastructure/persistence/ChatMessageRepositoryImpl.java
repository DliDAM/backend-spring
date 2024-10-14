package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatMessage;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final JpaChatMessageRepository jpaChatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage){return jpaChatMessageRepository.save(chatMessage);}

    @Override
    public Optional<List<ChatMessage>> findAllByChatRoom(final ChatRoom chatRoom) {
        return jpaChatMessageRepository.findAllByChatRoom(chatRoom);
    }

    @Override
    public ChatMessage findLastMessageInChatRoom(final Long chatRoomId) {
        return jpaChatMessageRepository.findLastMessageInChatRoom(chatRoomId);
    }
}
