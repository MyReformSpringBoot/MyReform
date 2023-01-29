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
    private List<Integer> categoryId;
    private String title;
    private String contents;

    @Column(nullable = false)
    private Integer price;

    @Builder
    public BoardUpdateDto(List<Integer> categoryId, String title, String contents, Integer price) {
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }
}
