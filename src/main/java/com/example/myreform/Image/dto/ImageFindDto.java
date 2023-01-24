package com.example.myreform.Image.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class ImageFindDto {

    @Column(name = "image_url")
    private String imageURL;

    @Builder
    public ImageFindDto(String imageURL) {
        this.imageURL = "/img/" + imageURL;
    }
}
