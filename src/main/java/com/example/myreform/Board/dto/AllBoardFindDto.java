package com.example.myreform.Board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllBoardFindDto {

    @Column(name = "board_id")
    private Long boardId;
    @Column(name = "category_id")
    private Integer categoryId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    private Integer price;
    //    private OneImageFindDto oneImageFindDto; // 빌드할 때에는 해당 데이터 x
    private String imageUrl;


    @Builder
    public AllBoardFindDto(Long boardId, Integer categoryId, String title, LocalDateTime updateAt, Integer price) {
        this.boardId = boardId;
        this.categoryId = categoryId;
        this.title = title;
        this.updateAt = updateAt;
        this.price = price;
    }
}
