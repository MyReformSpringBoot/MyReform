package com.example.myreform.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)//access 속성을 이용해서 동일한 패키지 내의 클래스에서만 객체를 생성할 수 있도록 제어합니다.
@Getter
@Table(name = "POST_IMAGE")
public class PostImage {//extends Image
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    private long post_image_id;

    @Column(name = "post_id")
    private long postId;
    @Column(name = "image_id")
    private long imageId;

    @Builder
    public PostImage(long postId, long imageId){
        this.postId = postId;
        this.imageId = imageId;
    }
}
