package com.example.myreform.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class UserFindDto {

    @Column(name = "user_id")
    private Long userId;
    private String email;
    private String id;
    private String nickname;
    private String introduction;

    @Builder
    public UserFindDto(Long userId, String email, String id, String nickname, String introduction) {
        this.userId = userId;
        this.email = email;
        this.id = id;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
