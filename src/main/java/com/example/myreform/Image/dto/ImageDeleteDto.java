package com.example.myreform.Image.dto;

import com.example.myreform.Image.domain.Image;
import com.example.myreform.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class ImageDeleteDto extends BaseEntity{
    @Column(name = "image_id")
    private long imageId;
    @Column(name = "image_url")
    private String imageURL;
 
    public Image toEntity(Long imageId){
        Image image = Image.builder()
                .imageId(imageId)
                .imageURL(imageURL)
                .build();
        return image;
    }

    @Builder
    public ImageDeleteDto(String imageURL, long imageId) {
        this.imageURL = imageURL;
        this.imageId = imageId;
    }

}
