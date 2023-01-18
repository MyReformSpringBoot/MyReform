package com.example.myreform.model.post;

import com.example.myreform.domain.BaseTimeEntity;
import com.example.myreform.domain.User;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostFindDto extends BaseTimeEntity {

    private User user;
    @Column(name = "category_id")
    private long categoryId;
    private String title;
    private String contents;
    private int status;

    @Builder
    public PostFindDto(User user, long categoryId, String title, String contents, int status) {
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.status = status;
    }
}
