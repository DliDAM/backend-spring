package com.dlidam.chat.domain;

import com.dlidam.global.common.entity.BaseCreateTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class ChatMessage extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String senderId;    // 보내는 사람

    private String senderName;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    // todo: 전송 여부, 읽음 여부

    public ChatMessage(
            final String senderId,
            final String senderName,
            final String message,
            final ChatRoom chatRoom){
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
