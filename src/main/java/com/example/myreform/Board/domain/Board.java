package com.example.myreform.Board.domain;

import com.example.myreform.Board.dto.AllBoardFindDto;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import com.example.myreform.Board.dto.OneBoardFindDto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.lang.Nullable;

import javax.persistence.*;

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

    @Transient
    private Integer categoryId;//조회를 위해

    // 연관관계 편의 메서드
    public void confirmUser(User user) {
        this.user = user;
        // 유저 게시글 추가
    }

    public void delete() {
        super.delete();
    }

    public OneBoardFindDto toFindDto() {
        return OneBoardFindDto.builder()
                .boardId(boardId)
                .user(user)
                .title(title)
                .contents(contents)
                .price(price)
                .updateAt(super.getUpdateAt())
                .build();
    }

    @Builder
    public Board(Long boardId, User user, Integer categoryId, String title, String contents, Integer price){
        this.boardId = boardId;
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }

    public AllBoardFindDto toAllBoardFindDto() {
        return AllBoardFindDto.builder()
                .boardId(boardId)
                //.categoryId(categoryId)
                .title(title)
                .updateAt(getUpdateAt())
                .price(price)
                .build();
    }
}
