package com.example.myreform.Image.service;

import com.example.myreform.Image.dto.ImageFindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    public List<ImageFindDto> getAllImages(List<ImageFindDto> imageFindDtos){
        return imageFindDtos;
    }

}
