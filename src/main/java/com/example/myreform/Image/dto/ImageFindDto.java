package com.example.myreform.Image.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class ImageFindDto {

    @Column(name = "image_url")
    private String imageURL;

    private final String uploadPath = "/img/";

    @Builder
    public ImageFindDto(String imageURL) {
        this.imageURL = uploadPath + imageURL;
    }
}
