package com.example.myreform.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image extends BaseTimeEntity{

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
