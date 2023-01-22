package com.example.myreform.Board.domain;

import com.example.myreform.User.domain.User;
import com.example.myreform.domain.BaseEntity;
import com.example.myreform.Board.dto.BoardFindDto;
import com.example.myreform.Board.dto.BoardUpdateDto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(length = 45, nullable = false, name = "title") // 제목 길이 체크
    private String title;

    @Column(nullable = false, name = "contents")
    private String contents;

    @Column(nullable = false)
    private Integer price;

    // 연관관계 편의 메서드
    public void confirmUser(User user) {
        this.user = user;
        // 유저 게시글 추가
    }

    public void delete() {
        super.delete();
    }

    public BoardFindDto toFindDto() {
        return BoardFindDto.builder()
                .boardId(boardId)
                .user(user)
                .categoryId(categoryId)
                .title(title)
                .contents(contents)
                .price(price)
                .createAt(super.getCreateAt())
                .updateAt(super.getUpdateAt())
                .status(super.getStatus())
                .build();
    }


    public BoardUpdateDto toBoardUpdateDto() {
        return BoardUpdateDto.builder()
                .boardId(boardId)
                .user(user)
                .categoryId(categoryId)
                .title(title)
                .contents(contents)
                .price(price)
                .build();
    }


    @Builder
    public Board(Long boardId, User user, Integer categoryId, String title, String contents, Integer price, LocalDateTime createAt, LocalDateTime updateAt){
        super(createAt, updateAt);
        this.boardId = boardId;
        this.user = user;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.price = price;
    }

}
