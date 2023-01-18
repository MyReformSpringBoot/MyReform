package com.example.myreform.domain.board;

import com.example.myreform.domain.BaseEntity;
import com.example.myreform.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(length = 45, nullable = false, name = "title") // 제목 길이 체크
    private String title;

    @Column(nullable = false, name = "contents")
    private String contents;


    // 연관관계 편의 메서드
    public void confirmUser(User user) {
        this.user = user;
        // 유저 게시글 추가
    }

    // 게시글 수정 메서드
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public BoardFindDto toDto() {
        return BoardFindDto.builder()
                .boardId(boardId)
                .user(user)
                .categoryId(categoryId)
                .title(title)
                .contents(contents)
                .createAt(super.getCreateAt())
                .updateAt(super.getUpdateAt())
                .status(super.getStatus())
                .build();
    }

    @Builder
    public Board(User user, Long category_id, String title, String contents){
        this.user = user;
        this.categoryId = category_id;
        this.title = title;
        this.contents = contents;
    }
}
