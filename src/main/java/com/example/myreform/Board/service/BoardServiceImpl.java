package com.example.myreform.Board.service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.domain.BoardImage;
import com.example.myreform.Board.dto.AllBoardFindDto;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.response.ResponseBoardEmpty;
import com.example.myreform.Image.domain.Image;
import com.example.myreform.Board.dto.OneBoardFindDto;
import com.example.myreform.Board.dto.BoardSaveDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.Image.controller.ImageUploadHandler;


import com.example.myreform.Board.repository.BoardImageRepository;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Image.dto.ImageFindDto;
import com.example.myreform.Image.repository.ImageRepository;

import com.example.myreform.Image.service.ImageService;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    @Autowired
    private final BoardRepository boardRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ImageUploadHandler imageUploadHandler;
    @Autowired
    private final BoardImageRepository boardImageRepository;
    @Autowired
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Value("${img.path}")
    private String IMG_PATH;

    @Override
    public Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception {
        Board board = boardSaveDto.toEntity();
        user = userRepository.findById(user.getUserId()).get();
        board.confirmUser(user);
        boardRepository.save(board);
        List<BoardImage> boardImages = boardImageRepository.saveAll(saveBoardImage(board.getBoardId(), files));

        OneBoardFindDto oneBoardFindDto = board.toFindDto();
        List<String> imageUrls = boardImages.stream()
                .map(x -> x.toImageFindDto()
                        .getImageURL())
                .collect(Collectors.toList());
        oneBoardFindDto.setImages(imageUrls);
        return new ResponseBoard(ExceptionCode.BOARD_CREATE_OK, oneBoardFindDto);
    }

    @Override
    public Object update(Long boardId, BoardUpdateDto boardUpdateDto, User user, List<MultipartFile> files) throws JsonProcessingException {

        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty() || boardOptional.get().getStatus() == 0) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_NOT_FOUND);
        }
        if (!boardOptional.get().getUser().getUserId().equals(user.getUserId())) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_UPDATE_INVALID);
        }

        Board board = boardUpdateDto.ToEntity(boardId);
        board.confirmUser(user);
        boardRepository.save(board);
        List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);
        deleteBoardImages(boardImages);

        try {
            boardImageRepository.saveAllAndFlush(saveBoardImage(boardId, files));
        } catch (Exception e) {
            System.out.println("파일을 업데이트하지 못했습니다.");
            throw new RuntimeException(e);//수정
        }
        OneBoardFindDto oneBoardFindDto = boardRepository.findBoardByBoardId(boardId).toFindDto();
        boardImages = boardImageRepository.findAllByBoardId(boardId);
        List<String> imageUrls = boardImages.stream()
                .map(x -> x.toImageFindDto()
                        .getImageURL())
                .collect(Collectors.toList());
        oneBoardFindDto.setImages(imageUrls);

        return new ResponseBoard(ExceptionCode.BOARD_UPDATE_OK, oneBoardFindDto);
    }

    @Override
    public Object delete(Long boardId, User user) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty() || boardOptional.get().getStatus() == 0) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_NOT_FOUND);
        }
        Board board = boardOptional.get();
        if (!board.getUser().getUserId().equals(user.getUserId())) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_DELETE_INVALID);
        }

        board.delete(); // status만 수정
        List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);
        deleteBoardImages(boardImages);
        boardRepository.save(board);
        return new ResponseBoardEmpty(ExceptionCode.BOARD_DELETE_OK);
    }

    @Transactional
    public void deleteBoardImages(List<BoardImage> boardImages) {
        for (BoardImage boardImage: boardImages) {
            Image image = boardImage.getImage();
            //실제로 폴더에서 삭제하는 코드 => status로 진행 시 실제로 삭제 안하기에 주석처리
            String path = new File(IMG_PATH).getAbsolutePath() + "/" + image.getImageURL();
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        boardImageRepository.deleteAll(boardImages);
    }

    @Override
    @Transactional(readOnly = true)
    public Object fetchBoardPagesBy(Long lastBoardId, int size, Integer categoryId, String keyword) {
        Page<Board> boards = fetchPages(lastBoardId, size, categoryId, keyword);
        List<AllBoardFindDto> allBoardFindDtos = boards.getContent().stream().map((x) -> x.toAllBoardFindDto()).collect(Collectors.toList());
        ExceptionCode exceptionCode = ExceptionCode.BOARD_GET_OK;
        List<ImageFindDto> imageFindDtos = new ArrayList<>();

        if (allBoardFindDtos.isEmpty()) {
            exceptionCode = ExceptionCode.BOARD_NOT_FOUND;
            return new ResponseBoardEmpty(exceptionCode);
        }
        for (AllBoardFindDto allBoardFindDto: allBoardFindDtos) {
            Long boardId = allBoardFindDto.getBoardId();
            List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);
            for(BoardImage boardImage : boardImages){
                if(boardImage.getImage().getImageURL().contains("first")){//first들어간게 대표이미지
                    ImageFindDto oneImageFindDto = boardImage.getImage().toOneImageFindDto();
                    imageFindDtos.add(oneImageFindDto);
                    allBoardFindDto.setImageUrl(oneImageFindDto.getImageURL());
                    break;
                }
            }
        }
        imageService.getAllImages(imageFindDtos);
        return new ResponseBoard(exceptionCode, allBoardFindDtos);
    }

    private Page<Board> fetchPages(Long lastBoardId, int size, Integer categoryId, String keyword)  {
        if (Optional.ofNullable(lastBoardId).isEmpty()) {
            lastBoardId = boardRepository.count() + 1;
        }
        PageRequest pageRequest = PageRequest.of(0, size);
        if (Optional.ofNullable(categoryId).isEmpty() && keyword == null) { // 카테고리나 검색안할 때
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(lastBoardId, 1, pageRequest);
        }
        if (Optional.ofNullable(categoryId).isEmpty()) { // 모든 카테고리에 대해 검색만 할 때
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsAndTitleContainingOrderByBoardIdDesc(lastBoardId, 1, keyword, pageRequest);
        }
        if (keyword == null) { // 검색을 안하고 카테고리만 찾아볼 때
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsAndCategoryIdEqualsOrderByBoardIdDesc(lastBoardId, 1, categoryId, pageRequest);
        }
        // 카테고리 설정 후 검색을 진행할 때
        return boardRepository.findAllByBoardIdLessThanAndStatusEqualsAndCategoryIdEqualsAndTitleContainingOrderByBoardIdDesc(lastBoardId, 1, categoryId.intValue(), keyword, pageRequest);
    }

    List<BoardImage> saveBoardImage(Long boardId, List<MultipartFile> files) throws Exception{
        List<Image> imageList = imageUploadHandler.parseImageInfo(boardId, files);

        List<BoardImage> boardImages = new ArrayList<>();
        for(Image image : imageList){
            BoardImage boardImage = BoardImage.builder()
                    .image(image)
                    .boardId(boardId)
                    .build();
            boardImages.add(boardImage);
        }
        return boardImages;
    }

}