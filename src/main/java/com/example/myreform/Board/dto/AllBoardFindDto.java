package com.example.myreform.Board.dto;

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
public class AllBoardFindDto {

    @Column(name = "board_id")
    private Long boardId;
    private List<Integer> categoryId;
    private String title;
    private String contents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    private String time;
    private Integer price;
    private Long countOfLike;
    private boolean likeOrNot;
    // user
    private String nickname;

    // image
    private List<String> imageUrl;


    @Builder
    public AllBoardFindDto(Long boardId, List<Integer> categoryId, String title,String contents,LocalDateTime updateAt ,Integer price, Long countOfLike,
                           String nickname,boolean likeOrNot, List<String> imageUrl) {
        this.boardId = boardId;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.updateAt = updateAt;
        this.time = Time.calculateTime(updateAt);
        this.price = price;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.likeOrNot = likeOrNot;
        this.countOfLike = countOfLike;
    }
}
