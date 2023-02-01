package com.example.myreform.User.dto;

import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto extends BaseEntity {
    @Column(name = "user_id")
    private Long userId;

    private String pw;
    private String email;
    private String nickname;
    private String introduction;


    @Builder
    public UserUpdateDto(String pw, String email, String nickname, String introduction) {
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
