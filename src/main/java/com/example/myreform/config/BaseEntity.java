package com.example.myreform.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
@DiscriminatorColumn
@NoArgsConstructor
@DynamicInsert
public abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false, name = "create_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    //db의 콜럼명 변경함  createAt  =>  create_at
    //Unknown column 'create_at' in 'field list' 에러가 나서 create_at로 변경
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(updatable = true, name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    @Column(name = "status", columnDefinition = "int default 1") // 상속관계 자동 매핑으로 자동으로 insert됨
    private int status = 1;

    protected void delete() {
        this.status = 0;
    }

    // 삭제된 데이터 복구
    protected void restore() {
        this.status = 1;
    }

}
