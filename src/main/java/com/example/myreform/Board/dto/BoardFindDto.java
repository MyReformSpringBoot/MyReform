package com.example.myreform.Board.dto;


import com.example.myreform.User.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardFindDto {

    @Column(name = "board_id")
    private Long boardId;
    private User user;
    @Column(name = "category_id")
    private Integer categoryId;
    private String title;
    private String contents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    private int status;

    private Integer price;

    @Builder
    public BoardFindDto(Long boardId, User user, Integer categoryId, String title, String contents, LocalDateTime createAt, LocalDateTime updateAt, int status, Integer price) {
        this.boardId = boardId;
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
        this.price = price;
    }
}
