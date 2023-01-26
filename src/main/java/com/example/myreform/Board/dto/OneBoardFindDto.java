package com.example.myreform.Board.dto;


import com.example.myreform.User.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class OneBoardFindDto {

    @Column(name = "board_id")
    private Long boardId;
    private Long userId;
    private String nickname;

    private List<Integer> categoryId;
    private String title;
    private String contents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    private Integer price;
    private List<String> imageUrls;

    @Builder
    public OneBoardFindDto(Long boardId, User user, List<Integer> categoryId, String title, String contents, LocalDateTime createAt, LocalDateTime updateAt, int status, Integer price, List<String> imageUrls) {
        this.boardId = boardId;
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.updateAt = updateAt;
        this.price = price;
        this.imageUrls = imageUrls;
    }
}
