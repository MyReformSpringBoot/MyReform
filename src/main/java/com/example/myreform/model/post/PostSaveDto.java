package com.example.myreform.model.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostSaveDto {
    private Long post_id;

    private User user; // 수정 필요

    private Long category_id;

    private String title;

    private String contents;

    public Post toEntity() {
        Post post = Post.builder()
                .user(user)
                .category_id(category_id)
                .title(title)
                .contents(contents)
                .build();
        return post;
    }

    @Builder
    public PostSaveDto(User user, Long category_id, String title, String contents) {
        this.user = user;
        this.category_id = category_id;
        this.title = title;
        this.contents = contents;
    }
}
