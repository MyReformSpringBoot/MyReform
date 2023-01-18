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
    private Long user_id;

    @Column(nullable = false) // 회원가입 파트에서 멤버관련 정보 설정
    private String email;
    private String id;
    @JsonIgnore//json으로 보내는 객체에 포함되지 않게 해줌
    private String pw;
    private String nickname;
    private String introduction;
    @JsonIgnore
    private String marketing;
}