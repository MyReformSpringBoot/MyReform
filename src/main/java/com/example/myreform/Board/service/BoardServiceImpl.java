package com.example.myreform.Board.service;

import com.example.myreform.Board.category.CategoryRepository;
import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.domain.BoardCategory;
import com.example.myreform.Board.domain.BoardImage;
import com.example.myreform.Board.dto.*;
import com.example.myreform.Board.repository.BoardCategoryRepository;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.response.ResponseBoardEmpty;
import com.example.myreform.Image.domain.Image;
import com.example.myreform.Image.controller.ImageUploadHandler;


import com.example.myreform.Board.repository.BoardImageRepository;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Image.dto.ImageFindDto;

import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private final BoardCategoryRepository boardCategoryRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    @Value("${img.path}")
    private String IMG_PATH;

    @Override
    public Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception {
        user = userRepository.findById(user.getUserId()).get();
        Board board = boardSaveDto.toEntity(user);

        List<BoardCategory> boardCategories = new ArrayList<>();
        for (Integer categoryId : boardSaveDto.getCategoryId()) {
            boardCategories.add(new BoardCategory(board, categoryRepository.findByCategoryId(categoryId)));
        }
        boardCategoryRepository.saveAll(boardCategories);
        boardImageRepository.saveAll(saveBoardImage(board, files));
        return new ResponseBoard(ExceptionCode.BOARD_CREATE_OK, board.toOneBoardFindDto());
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

        Board board = boardOptional.get();
        board.update(boardUpdateDto);

        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByBoard_BoardId(boardId);
        boardCategoryRepository.deleteAll(boardCategories);
        boardCategoryRepository.flush();
        boardCategories.clear();
        for (Integer i : boardUpdateDto.getCategoryId()) {
            boardCategories.add(new BoardCategory(board, categoryRepository.findByCategoryId(i)));
        }
        boardCategoryRepository.saveAllAndFlush(boardCategories);

        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(board);
        deleteBoardImages(boardImages);

        try {
            boardImageRepository.saveAllAndFlush(saveBoardImage(board, files));
        } catch (Exception e) {
            System.out.println("파일을 업데이트하지 못했습니다.");
            throw new RuntimeException(e);//수정
        }

        List<Integer> categoryId = boardUpdateDto.getCategoryId();

        boardImages = boardImageRepository.findAllByBoard(board);
        List<String> imageUrls = boardImages.stream()
                .map(x -> x.toImageFindDto()
                        .getImageURL())
                .collect(Collectors.toList());
        OneBoardFindDto oneBoardFindDto = boardRepository.findBoardByBoardId(boardId).toOneBoardFindDto(categoryId, imageUrls);

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

        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByBoard_BoardId(boardId);
        boardCategoryRepository.deleteAll(boardCategories);

        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(board);
        deleteBoardImages(boardImages);
        return new ResponseBoardEmpty(ExceptionCode.BOARD_DELETE_OK);
    }

    @Override
    public Object getOneBoard(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty() || boardOptional.get().getStatus() == 0) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_NOT_FOUND);
        }
        Board board = boardOptional.get();
        List<Integer> categoryId = boardCategoryRepository.findAllByBoard_BoardId(boardId).stream()
                .map(x -> x.getCategory().getCategoryId())
                .collect(Collectors.toList());
        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(board);
        List<String> imageUrls = boardImages.stream()
                .map(x -> x.toImageFindDto()
                        .getImageURL())
                .collect(Collectors.toList());
        OneBoardFindDto oneBoardFindDto = board.toOneBoardFindDto(categoryId, imageUrls);

        return new ResponseBoard(ExceptionCode.BOARD_GET_OK, oneBoardFindDto);
    }

    @Transactional
    public void deleteBoardImages(List<BoardImage> boardImages) {
        for (BoardImage boardImage : boardImages) {
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

    List<BoardImage> saveBoardImage(Board board, List<MultipartFile> files) throws Exception{
        List<Image> imageList = imageUploadHandler.parseImageInfo(board.getBoardId(), files);
        List<BoardImage> boardImages = new ArrayList<>();
        for(Image image : imageList){
            BoardImage boardImage = BoardImage.builder()
                    .image(image)
                    .board(board)
                    .build();
            boardImages.add(boardImage);
        }
        return boardImages;
    }

    @Override
    @Transactional(readOnly = true)
    public Object fetchBoardPagesBy(Long lastBoardId, int size, Integer categoryId, String keyword) {
        Page<Board> boards = fetchPages(lastBoardId, size, categoryId, keyword);
        List<AllBoardFindDto> allBoardFindDtos = boards.getContent().stream()
                .map((x) -> x.toAllBoardFindDto(getCategoryId(x.getBoardId())))
                .collect(Collectors.toList());
        ExceptionCode exceptionCode = ExceptionCode.BOARD_GET_OK;
        if (allBoardFindDtos.isEmpty()) {
            exceptionCode = ExceptionCode.BOARD_NOT_FOUND;
            return new ResponseBoardEmpty(exceptionCode);
        }

        return new ResponseBoard(exceptionCode,allBoardFindDtos);
    }

    private Page<Board> fetchPages(Long lastBoardId, int size, Integer categoryId, String keyword)  {
        if (Optional.ofNullable(lastBoardId).isEmpty()) {
            lastBoardId = boardRepository.count() + 1;
        }
        PageRequest pageRequest = PageRequest.of(0, size);
        Pageable pageable = PageRequest.of(0, size);
        if (Optional.ofNullable(categoryId).isEmpty() && keyword == null) { // 카테고리나 검색안할 때
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(lastBoardId, 1, pageRequest);
        }
        if (Optional.ofNullable(categoryId).isEmpty()) { // 모든 카테고리에 대해 검색만 할 때
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsAndTitleContainingOrderByBoardIdDesc(lastBoardId, 1, keyword, pageRequest);
        }
        if (keyword == null) { // 검색을 안하고 카테고리만 찾아볼 때
            Page<BoardCategory> boardCategoryPage = boardCategoryRepository.findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_StatusEqualsOrderByBoardDesc(lastBoardId, categoryId, 1, pageRequest);
            Page<Board> boardPage = new PageImpl<>(boardCategoryPage.stream()
                    .map(x -> x.getBoard())
                    .collect(Collectors.toList()));
            return boardPage;
        }
        // 카테고리 설정 후 검색을 진행할 때
        Page<BoardCategory> boardCategoryPage = boardCategoryRepository.findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_TitleContainingAndBoard_StatusEqualsOrderByBoardDesc
                (lastBoardId, categoryId, keyword,1, pageRequest);
        Page<Board> boardPage = new PageImpl<>(boardCategoryPage.stream()
                .map(x -> x.getBoard())
                .collect(Collectors.toList()));
        return boardPage;

    }

    public List<Integer> getCategoryId(Long boardId) {
        return boardCategoryRepository.findAllByBoard_BoardId(boardId).stream()
                .map(x -> x.getCategory().getCategoryId())
                .collect(Collectors.toList());
    }

    private List<String> getUrlString(List<BoardImage> boardImages) {
        return boardImages.stream()
                .map(x -> x.toImageFindDto()
                        .getImageURL())
                .collect(Collectors.toList());
    }
}