package com.example.myreform.Image.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class ImageFindDto {

    @Column(name = "image_url")
    private String imageURL;

    @Builder
    public ImageFindDto(String imageURL) {
        this.imageURL = imageURL;
    }
}
