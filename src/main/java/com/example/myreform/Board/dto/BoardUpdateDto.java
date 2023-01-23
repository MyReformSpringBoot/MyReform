package com.example.myreform.Board.dto;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDto extends BaseEntity {
    @Column(name = "board_id")
    private Long boardId;
    private User user;
    @Column(name = "category_id")
    private int categoryId;
    private String title;
    private String contents;

    @Column(nullable = false)
    private Integer price;

    public Board ToEntity(Long boardId){

        Board board = Board.builder()
                .user(user)
                .boardId(boardId)
                .categoryId(categoryId)
                .title(title)
                .contents(contents)
                .price(price)
                .build();
        return board;
    }
}
