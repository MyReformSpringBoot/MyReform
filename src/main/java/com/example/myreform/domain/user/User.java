package com.example.myreform.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false) // 회원가입 파트에서 멤버관련 정보 설정
    private String email;
    private String id;
    @JsonIgnore//json으로 보내는 객체에 포함되지 않게 해줌
    private String pw;
    private String nickname;
    private String introduction;
    @JsonIgnore
    private String marketing;

    public User(Long userId) {
        System.out.println(userId);
        this.userId = userId; // 어떤 정보를 클라이언트에서 갖는지 알지 못해서 이를 유저 정보와 게시물 정보 통합 이후에 처리
        this.email = "";
        this.pw = "";
        this.nickname = "";
    }
}
