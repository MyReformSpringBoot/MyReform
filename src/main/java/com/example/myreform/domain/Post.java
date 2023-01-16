package com.example.myreform.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    //    @ManyToOne
    //    @JoinColumn(name = "user_id")
    private Long user_id2; // 수정 필요

    private Long category_id;

    @Column(length = 45, nullable = false) // 제목 길이 체크
    private String title;

    @Column(nullable = false)
    private String contents;


    // 게시글 수정 메서드
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }
}
