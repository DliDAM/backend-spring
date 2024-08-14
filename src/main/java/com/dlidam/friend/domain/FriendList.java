package com.dlidam.friend.domain;

import com.dlidam.global.common.entity.BaseCreateTimeEntity;
import com.dlidam.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.dlidam.friend.domain.FriendType.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FriendList extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String friendId;

    @Enumerated(value = STRING)
    private FriendType friendType;

    @Builder
    private FriendList(
            final User user,
            final String friendId
    ){
        this.user = user;
        this.friendId = friendId;
        this.friendType = FRIEND;
    }

}
