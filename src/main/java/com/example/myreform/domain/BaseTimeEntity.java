package com.example.myreform.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@DiscriminatorColumn
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false, name = "createAt", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(updatable = true, name = "updateAt")
    private LocalDateTime updateAt;
}
