package com.example.myreform.Board.dto;

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
    private List<Integer> categoryId;//조회를 위해
    private String title;
    private String contents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    private Integer price;
    //    private OneImageFindDto oneImageFindDto; // 빌드할 때에는 해당 데이터 x
    private String imageUrl;


    @Builder
    public AllBoardFindDto(Long boardId, List<Integer> categoryId, String title,String contents, LocalDateTime updateAt, Integer price) {
        this.boardId = boardId;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.updateAt = updateAt;
        this.price = price;
    }
}
