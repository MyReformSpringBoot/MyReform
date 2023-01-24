package com.example.myreform.Image.service;

import com.example.myreform.Image.domain.Image;
import com.example.myreform.Image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
    @Autowired
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public List<Image> getImages(){
        List<Image> images = imageRepository.findAll();
        return findAll(images);
    }
    public List<Image> findAll(List<Image> images){
        List<Image> returnImages = new ArrayList<>();
        for (Image image:images){
            if(image.getImageURL().contains("first")){
                returnImages.add(image);
            }
        }
        return returnImages;
    }

}
