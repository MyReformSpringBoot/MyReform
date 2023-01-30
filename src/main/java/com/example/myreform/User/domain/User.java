package com.example.myreform.User.domain;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.dto.UserFindDto;
import com.example.myreform.User.dto.UserUpdateDto;
import com.example.myreform.config.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@EntityListeners(AuditingEntityListener.class) // 자동으로 날짜, 시간
public class User extends BaseEntity {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 10, unique = true)
    private String id;
    @Column(nullable = false)
    @JsonIgnore//json으로 보내는 객체에 포함되지 않게 해줌
    private String pw;
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String introduction;

    @Column(nullable = false)
    @JsonIgnore
    private boolean marketing;



    public enum Role {
        USER, MANAGER, ADMIN
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    List<Board> board = new ArrayList<>();

    @Builder
    public User(Long userId, String id, String pw, String nickname, String email,
                Role role, boolean marketing) {
        this.userId = userId; // 어떤 정보를 클라이언트에서 갖는지 알지 못해서 이를 유저 정보와 게시물 정보 통합 이후에 처리
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.marketing = marketing;
    }

    public UserFindDto toFindDto(Optional<User> userOp) {
        return UserFindDto.builder()
                .userId(userId)
                .email(email)
                .id(id)
                .nickname(nickname)
                .introduction(introduction)
                .build();
    }

    public void update(UserUpdateDto userUpdateDto) {
        this.nickname = userUpdateDto.getNickname();
        this.introduction = userUpdateDto.getIntroduction();
    }

}
