package com.example.myreform.domain.board;

import com.example.myreform.domain.BaseEntity;
import com.example.myreform.domain.user.User;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSaveDto extends BaseEntity {

    private User user;
    @Column(name = "category_id")
    private Integer categoryId;
    private String title;
    private String contents;

    public Board toEntity() {
        Board board = Board.builder()
                .user(user)
                .category_id(categoryId)
                .title(title)
                .contents(contents)
                .build();
        return board;
    }

    @Builder
    public BoardSaveDto(User user, Integer categoryId, String title, String contents) {
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
    }
}
