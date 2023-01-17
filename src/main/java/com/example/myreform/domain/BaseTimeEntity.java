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
    @Column(updatable = false, name = "create_at", nullable = false)
    //db의 콜럼명 변경함  createAt  =>  create_at
    //Unknown column 'create_at' in 'field list' 에러가 나서 create_at로 변경
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(updatable = true, name = "update_at")
    private LocalDateTime updateAt;
}
