package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.dto.ChatRoomWithLastMessageDTO;
import com.dlidam.chat.domain.repository.ChatRoomRepository;
import com.dlidam.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JpaChatRoomRepository jpaChatRoomRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom){
        return jpaChatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findById(Long chatRoomId){return jpaChatRoomRepository.findById(chatRoomId);}

    @Override
    public Optional<ChatRoom> findBySenderAndReceiver(final User sender, final User receiver) {
        return jpaChatRoomRepository.findBySenderAndReceiver(sender, receiver);
    }

    @Override
    public List<ChatRoomWithLastMessageDTO> findAllChatRoomByUserIdOrderByLastMessage(final Long userId) {
        return jpaChatRoomRepository.findAllChatRoomByUserIdOrderByLastMessage(userId);
    }

//    @Override
//    public List<ChatRoom> findAllByUser(User user){return jpaChatRoomRepository.findAllByUser(user);}
}
