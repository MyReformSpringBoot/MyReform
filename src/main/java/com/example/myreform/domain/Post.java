package com.example.myreform.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id2") // 수정 필요
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

    @Builder
    public Post(User user, Long category_id, String title, String contents){
        this.user = user;
        this.categoryId = category_id;
        this.title = title;
        this.contents = contents;
    }
}
