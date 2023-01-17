package com.example.myreform.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert//null인 필드 값을 제시켜주는 역할을 하기 때문에 defualt로 잡혀있는 값으로 insert
@DynamicUpdate
@Table(name = "POST")
public class Post extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    @Column(name = "post_id")
    private long postId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "category_id")
    private long categoryId;

    private String title;
    private String  contents;
    @Column(name = "create_at")
    private Timestamp createAt;
    @Column(name = "update_at")
    private Timestamp updateAt;
    private int status;

    @Builder//빌더 패턴을 이용하면 어떤 멤버에 어떤 값을 세팅하는지 직관적으로 확인이 가능
    public Post(long userId, long categoryId, String title, String contents,Timestamp createAt, Timestamp updateAt, int status) {
        super(createAt, updateAt);
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.contents = contents;
        this.status = status;

    }


    //SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
    //(sdf.format(updateAt));

}
