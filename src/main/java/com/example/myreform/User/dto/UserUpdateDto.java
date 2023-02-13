package com.example.myreform.User.dto;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto extends BaseEntity {
    @Column(name = "user_id")
    private Long userId;
    private String nickname;
    private String introduction;

    private List<Board> likeBoards = new ArrayList<>();


    @Builder
    public UserUpdateDto( String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
