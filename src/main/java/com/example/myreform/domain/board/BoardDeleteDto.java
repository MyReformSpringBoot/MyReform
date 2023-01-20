package com.example.myreform.domain.board;

import com.example.myreform.domain.BaseEntity;
import com.example.myreform.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDeleteDto {
    @Column(name = "board_id")
    private Long boardId;
    private User user;
    @Column(name = "category_id")
    private int categoryId;
    private String title;
    private String contents;
    @Column(nullable = false)
    private Integer price;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int status;

    public Board toEntity(){
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

    @Builder
    public BoardDeleteDto(Long boardId, User user, int categoryId, String title, String contents, Integer price, LocalDateTime createAt, LocalDateTime updateAt, int status) {
        this.boardId = boardId;
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }
}
