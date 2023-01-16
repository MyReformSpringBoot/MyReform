package com.example.myreform.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String pw;
    private String nickname;

    private int marketing;

    @Builder
    public UserDTO(String id, String pw, String nickname, String email, int marketing) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.marketing = marketing;
    }

    // 비밀번호 encode 처리
    public User toUser(String encodePw) {
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .pw(encodePw)
                .role(User.Role.USER)
                .build();
    }
}
