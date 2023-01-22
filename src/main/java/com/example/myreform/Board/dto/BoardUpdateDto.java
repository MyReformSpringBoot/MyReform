package com.example.myreform.Board.dto;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import com.example.myreform.domain.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDto extends BaseEntity{
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
                .createAt(super.getCreateAt())
                .updateAt(super.getUpdateAt())
                .build();
        return board;
    }

    @Builder
    public BoardUpdateDto(Long boardId, User user, int categoryId, String title, String contents, Integer price) {
        this.boardId = boardId;
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }
}
