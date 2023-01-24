package com.example.myreform.Board.dto;

import com.example.myreform.Image.domain.Image;
import com.example.myreform.Image.dto.ImageFindDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardImageFindDto{//어떤 게시글의 이미지url인지

    @Column(name = "board_id")
    private Long boardId;

    private String imageURL;

    @Builder
    public BoardImageFindDto(Long boardId, String imageURL){
        this.boardId = boardId;
        this.imageURL = imageURL;
    }
}
