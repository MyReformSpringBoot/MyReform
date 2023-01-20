package com.example.myreform.User.dto;

import com.example.myreform.User.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDTO {
    private String id;
    private String pw;

    @Builder
    public UserLoginDTO(String id, String pw) {
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
