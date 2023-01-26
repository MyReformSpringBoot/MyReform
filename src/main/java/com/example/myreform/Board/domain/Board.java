package com.example.myreform.Board.domain;

import com.example.myreform.Board.dto.AllBoardFindDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import com.example.myreform.Board.dto.OneBoardFindDto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "board")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicInsert
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 45, nullable = false, name = "title") // 제목 길이 체크
    private String title;

    @Column(nullable = false, name = "contents")
    private String contents;

    @Column(nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages = new ArrayList<>();

    public void delete() {
        super.delete();
    }

    public OneBoardFindDto toOneBoardFindDto(List<Integer> categoryId, List<String> imageUrls) {
        return OneBoardFindDto.builder()
                .boardId(boardId)
                .categoryId(categoryId)
                .user(user)
                .title(title)
                .contents(contents)
                .price(price)
                .updateAt(super.getUpdateAt())
                .imageUrls(imageUrls)
                .build();
    }

    @Builder
    public Board(Long boardId, User user, String title, String contents, Integer price){
        this.boardId = boardId;
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }

    public AllBoardFindDto toAllBoardFindDto(List<Integer> categoryId) {
        return AllBoardFindDto.builder()
                .categoryId(categoryId)
                .boardId(boardId)
                .title(title)
                .contents(contents)
                .updateAt(getUpdateAt())
                .price(price)
                .nickname(user.getNickname())
                .imageUrl(boardImages.stream()
                        .map(x -> x.getImage().toOneImageFindDto().getImageURL())
                        .collect(Collectors.toList()))
                .build();
    }

    public void update(BoardUpdateDto boardUpdateDto) {
        this.title = boardUpdateDto.getTitle();
        this.contents = boardUpdateDto.getContents();
        this.price = boardUpdateDto.getPrice();
    }
}
