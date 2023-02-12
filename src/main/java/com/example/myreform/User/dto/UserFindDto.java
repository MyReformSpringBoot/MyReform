package com.example.myreform.User.dto;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.dto.AllBoardFindDto;
import com.example.myreform.Board.dto.OneBoardFindDto;
import com.example.myreform.Like.domain.Like;
import lombok.*;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserFindDto {

    private String email;
    private String id;
    private String nickname;
    private String introduction;
    private List<AllBoardFindDto> likeBoards;


    @Builder
    public UserFindDto(String email, String id, String nickname, String introduction,
                       List<AllBoardFindDto> likeBoards) {
        this.email = email;
        this.id = id;
        this.nickname = nickname;
        this.introduction = introduction;
        this.likeBoards = likeBoards;
    }
}
