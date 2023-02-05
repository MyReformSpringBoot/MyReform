package com.example.myreform.chat.domain;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_BOARD_CHATROOM")
public class UserBoardChatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userBoardChatroomId;

    @OneToOne
    @JoinColumn(name = "sender_user_id")
    private User sender;

    @OneToOne
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "board_id")
    private Board board;

    private LocalDateTime lastTime;

    @Builder
    public UserBoardChatroom(Board board, User owner, User sender){
        this.board = board;
        this.owner = owner;
        this.sender = sender;
    }

    public void setLastTime(LocalDateTime time) {
        this.lastTime = time;
    }
}
