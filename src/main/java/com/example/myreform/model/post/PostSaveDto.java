package com.example.myreform.model.post;

import com.example.myreform.domain.BaseTimeEntity;
import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostSaveDto extends BaseTimeEntity {

    private User user; // 수정 필요

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
