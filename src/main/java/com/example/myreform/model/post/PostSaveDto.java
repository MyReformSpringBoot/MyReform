package com.example.myreform.model.post;

import com.example.myreform.domain.BaseEntity;
import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostSaveDto extends BaseEntity {

    private User user;
    @Column(name = "category_id")
    private long categoryId;
    private String title;
    private String contents;
    private int status;

    public Post toEntity() {
        Post post = Post.builder()
                .user(user)
                .category_id(categoryId)
                .title(title)
                .contents(contents)
                .build();
        return post;
    }

    @Builder
    public PostSaveDto(User user, Long categoryId, String title, String contents) {
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
    }
}
