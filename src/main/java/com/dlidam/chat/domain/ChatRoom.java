package com.dlidam.chat.domain;

import com.dlidam.global.common.entity.BaseTimeEntity;
import com.dlidam.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessage = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String lastChatMessage;

    private Boolean user1Connect = false;

    private Boolean user2Connect = false;

    public ChatRoom(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

    public void setLastChatMessage(String lastChatMessage){this.lastChatMessage = lastChatMessage;}

    public void setUser1ConnectTrue(){
        this.user1Connect = true;
    }

    public void setUser2ConnectTrue(){
        this.user2Connect = true;
    }


}
