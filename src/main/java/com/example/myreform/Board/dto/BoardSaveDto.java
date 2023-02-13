package com.example.myreform.Board.dto;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;

import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSaveDto extends BaseEntity {
    private String id;
    private List<Integer> categoryId;//배열 저장을 위해 List로 만듬
    private String title;
    private String contents;
    private Integer price;

    public Board toEntity(User user) {
        Board board = Board.builder()
                .user(user)
                .title(title)
                .contents(contents)
                .price(price)
                .build();
        return board;
    }

    @Builder
    public BoardSaveDto(String id, List<Integer> categoryId, String title, String contents, Integer price) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }
}
