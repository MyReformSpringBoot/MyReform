package com.example.myreform.domain.board;

import com.example.myreform.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long imageId;

    @Column(name = "image_url")
    private String imageURL;

    @Builder
    public Image(String imageURL) {
        this.imageURL = imageURL;
    }

}
