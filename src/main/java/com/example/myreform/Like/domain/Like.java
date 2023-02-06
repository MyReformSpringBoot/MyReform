package com.example.myreform.Like.domain;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "LIKE_USER")
@NoArgsConstructor
@RequiredArgsConstructor
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_user_id")
    private Long likeUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    Board board;

    @Builder
    public Like(User user, Board board){
        this.user = user;
        this.board = board;
    }
}
