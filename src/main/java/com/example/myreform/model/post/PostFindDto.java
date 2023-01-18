package com.example.myreform.model.post;

import com.example.myreform.domain.BaseEntity;
import com.example.myreform.domain.User;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostFindDto extends BaseEntity {

    private User user;
    @Column(name = "category_id")
    private long categoryId;
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int status;

    @Builder
    public PostFindDto(User user, long categoryId, String title, String contents, LocalDateTime createAt, LocalDateTime updateAt, int status) {
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }
}
