package com.example.myreform.User.dto;

import com.example.myreform.User.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginDTO {
    private String id;
    private String pw;

    @Builder
    public LoginDTO(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    // 비밀번호 encode 처리
    public User toUser(String encodePw) {
        return User.builder()
                .id(id)
                .pw(encodePw)
                .build();
    }
}
