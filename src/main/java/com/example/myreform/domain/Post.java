package com.example.myreform.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne
    @JoinColumn(name = "user_id2") // 수정 필요
    private User user;

    private Long category_id;

    @Column(length = 45, nullable = false) // 제목 길이 체크
    private String title;

    @Column(nullable = false)
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
//        this.post_id = post_id;
        this.user = user;
        this.category_id = category_id;
        this.title = title;
        this.contents = contents;
    }
}
