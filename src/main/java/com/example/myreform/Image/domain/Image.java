package com.example.myreform.Image.domain;

import com.example.myreform.Image.dto.ImageFindDto;
import com.example.myreform.config.BaseEntity;
//import com.example.myreform.Image.dto.ImageDeleteDto;
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
    public Image(String imageURL, long imageId) {
        this.imageURL = imageURL;
        this.imageId = imageId;
    }

    public ImageFindDto toOneImageFindDto() {
        return ImageFindDto.builder()
                .imageURL(imageURL)
                .build();
    }

}
