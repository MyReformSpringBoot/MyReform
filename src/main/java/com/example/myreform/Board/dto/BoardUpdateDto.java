package com.example.myreform.Board.dto;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateDto extends BaseEntity {
    @Column(name = "board_id")
    private Long boardId;
    private User user;
    private List<Integer> categoryId;//배열 저장을 위해 List로 만듬
    private String title;
    private String contents;

    @Column(nullable = false)
    private Integer price;

    public Board ToEntity(Long boardId){

        Board board = Board.builder()
                .user(user)
                .boardId(boardId)
                .title(title)
                .contents(contents)
                .price(price)
                .build();
        return board;
    }

    @Builder
    public BoardUpdateDto(User user, List<Integer> categoryId, String title, String contents, Integer price) {
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }
}
