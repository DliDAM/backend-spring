package com.dlidam.chat.infrastructure.persistence;

import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.domain.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JpaChatRoomRepository jpaChatRoomRepository;

    @Override
    public ChatRoom findBySenderIdAndReceiverId(final Long senderId, final Long receiverId) {
        return jpaChatRoomRepository.findBySenderIdAndReceiverId(senderId,receiverId);
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom){
        return jpaChatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findById(Long chatRoomId){return jpaChatRoomRepository.findById(chatRoomId);}

//    @Override
//    public List<ChatRoom> findAllByUser(User user){return jpaChatRoomRepository.findAllByUser(user);}
}
