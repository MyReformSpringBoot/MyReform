package com.example.myreform.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)//access 속성을 이용해서 동일한 패키지 내의 클래스에서만 객체를 생성할 수 있도록 제어합니다.
@Getter
@AllArgsConstructor
//@SuperBuilder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    @Column(name = "image_id")
    private long imageId;
    @Column(name = "image_url")
    private String imageURL;
    @UpdateTimestamp
    @Column(name = "update_at")
    private Timestamp updateAt;

    @Builder
    public Image(String imageURL, Timestamp updateAt) {
        this.imageURL = imageURL;
        this.updateAt = updateAt;
    }
}
