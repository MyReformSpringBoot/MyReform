package com.example.myreform.Image.controller;

import com.example.myreform.Image.domain.Image;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.System.getProperty;

@Component

public class ImageUploadHandler {

    @Value("${img.path}")
    private String IMG_PATH;

    public List<Image> parseImageInfo(Long boardId, @NonNull List<MultipartFile> multipartFiles) throws Exception {

        // 반환을 할 파일 리스트
        List<Image> fileList = new ArrayList<>();

        // 파일이 빈 것이 들어오면 빈 것을 반환
        if (multipartFiles.isEmpty()) {
            return fileList;
        }

        // 파일 이름을 업로드 한 날짜로 바꾸어서 저장할 것이다
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        // 파일이 빈 것이 들어오면 빈 것을 반환
        if (multipartFiles.isEmpty()) {
            return fileList;
        }
        // 프로젝트 폴더에 저장하기 위해 절대경로를 설정
        String absolutePath = new File(IMG_PATH).getAbsolutePath() + "/";

        // 경로를 지정하고 그곳에다가 저장
        String path = current_date;
        File file = new File(IMG_PATH + "/" + path);
        System.out.println(1);
        // 저장할 위치의 디렉토리가 존지하지 않을 경우
        if (!file.exists()) {
            System.out.println("2 = " + 2);
            // mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
            file.mkdirs();
        }

        for (MultipartFile multipartFile : multipartFiles) {
            // 파일이 비어 있지 않을 때 작업을 시작해야 오류가 나지 않는다
            if (!multipartFile.isEmpty()) {
                // jpeg, png 파일들만 받아서 처리할 예정
                String contentType = multipartFile.getContentType();
                String contentName = multipartFile.getOriginalFilename();
                String originalFileExtension;
                // 확장자 명이 없으면 이 파일은 잘 못 된 것
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentName.contains("jpg")) {
                        originalFileExtension = ".jpg";
                    } else if (contentName.contains("jpeg")) {
                        originalFileExtension = ".jpeg";
                    } else if (contentName.contains("png")) {
                        originalFileExtension = ".png";
                    }
                    // 다른 파일 명이면 아무 일 하지 않음
                    else {
                        break;
                    }
                }

                // 각 이름은 겹치면 안되므로 uuid(경로에 포함)
                String imageName;
                String uuid = UUID.randomUUID().toString();
                imageName = uuid + originalFileExtension;

                // 생성 후 리스트에 추가
                Image image = Image.builder()
                        .imageURL(path+"/"+imageName)
                        .build();
                fileList.add(image);

                // 저장된 파일로 변경하여 이를 보여주기 위함
                file = new File(absolutePath + path + "/" + imageName);
                multipartFile.transferTo(file);
            }
        }

        return fileList;
    }

}