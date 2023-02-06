package com.example.myreform.User.dto;

import com.example.myreform.Board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserFindDto {

    @Column(name = "user_id")
    private Long userId;
    private String email;
    private String id;
    private String nickname;
    private String introduction;
    private List<Board> likeBoards = new ArrayList<>();

    @Builder
    public UserFindDto(Long userId, String email, String id, String nickname, String introduction, List<Board> likeBoards) {
        this.userId = userId;
        this.email = email;
        this.id = id;
        this.nickname = nickname;
        this.introduction = introduction;
        this.likeBoards = likeBoards;
    }
}
