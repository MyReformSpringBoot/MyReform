package com.example.myreform.domain;

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
    private String pw;
    private String nickname;
    private String intruduction;
    private String marketing;
}
