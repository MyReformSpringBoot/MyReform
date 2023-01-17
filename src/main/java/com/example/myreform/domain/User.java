package com.example.myreform.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 자동으로 날짜, 시간
public class User {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @Column(length = 10, unique = true)
    private String id;
    @Column(nullable = false)
    private String pw;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String introduction;

    @Column(nullable = false)
    private boolean marketing;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ColumnDefault("1")
    private int state;


    public enum Role {
        USER, MANAGER, ADMIN
    }

    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public User(String id, String pw, String nickname, String email,
                Role role, boolean marketing) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.marketing = marketing;
    }
}
