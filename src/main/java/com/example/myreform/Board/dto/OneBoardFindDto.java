package com.example.myreform.Board.dto;


import com.example.myreform.User.domain.User;
import com.example.myreform.config.Time;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OneBoardFindDto {

    @Column(name = "board_id")
    private Long boardId;
    private Long userId;
    private String id;
    private String nickname;

    private List<Integer> categoryId;
    private String title;
    private String contents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updateAt;
    public String time;
    private Integer price;
    private Long countOfLike;
    private boolean likeOrNot;
    private List<String> imageUrl;

    @Builder
    public OneBoardFindDto(Long boardId, User user, List<Integer> categoryId, String title, String contents, LocalDateTime updateAt,
                           Integer price, Long countOfLike,boolean likeOrNot, List<String> imageUrl) {
        this.boardId = boardId;
        this.nickname = user.getNickname();
        this.userId = user.getUserId();
        this.id = user.getId();
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.updateAt = updateAt;
        this.time = Time.calculateTime(updateAt);
        this.price = price;
        this.countOfLike = countOfLike;
        this.likeOrNot =likeOrNot;
        this.imageUrl = imageUrl;
    }
}
