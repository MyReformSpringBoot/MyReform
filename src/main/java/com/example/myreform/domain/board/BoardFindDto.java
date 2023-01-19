package com.example.myreform.domain.board;

import com.example.myreform.domain.BaseEntity;
import com.example.myreform.domain.user.User;
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
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int status;

    @Builder
    public BoardFindDto(Long boardId, User user, Integer categoryId, String title, String contents, LocalDateTime createAt, LocalDateTime updateAt, int status) {
        this.boardId = boardId;
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }
}
