package com.example.myreform.Image.service;

import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.response.ResponseBoardEmpty;
import com.example.myreform.Board.service.BoardService;
import com.example.myreform.Image.domain.Image;
import com.example.myreform.Image.dto.ImageFindDto;
import com.example.myreform.Image.repository.ImageRepository;
import com.example.myreform.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    public List<ImageFindDto> getAllImages(List<ImageFindDto> imageFindDtos){
        return imageFindDtos;
    }

}
