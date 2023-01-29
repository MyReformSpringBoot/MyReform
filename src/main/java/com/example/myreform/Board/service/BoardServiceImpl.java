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

import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) {

            user = userRepository.findById(user.getUserId()).get(); // db에 저장된 객체와의 연관관계 부여를 위해 find 진행
            Board board = boardSaveDto.toEntity(user);
        try {
            List<BoardCategory> boardCategories = new ArrayList<>();
            for (Integer categoryId : boardSaveDto.getCategoryId()) {
                boardCategories.add(new BoardCategory(board, categoryRepository.findByCategoryId(categoryId)));
            }
            boardCategoryRepository.saveAll(boardCategories);
            boardImageRepository.saveAll(saveBoardImage(board, files));
        } catch (Exception exception) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_CREATE_ERROR);
        }

        return new ResponseBoard(ExceptionCode.BOARD_CREATE_OK, board.toOneBoardFindDto());
    }

    @Override
    public Object update(Long boardId, BoardUpdateDto boardUpdateDto, User user, List<MultipartFile> files) {

        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByBoard_BoardId(boardId);
        try {
            validateBoard(boardCategories, user, ExceptionCode.BOARD_UPDATE_INVALID);
        } catch (IllegalArgumentException exception) {
            ExceptionCode exceptionCode = ExceptionCode.findExceptionCodeByCode(exception.getMessage());
            return new ResponseBoardEmpty(exceptionCode);
        }
        Board board = boardCategories.get(0).getBoard();

        try {
            board.update(boardUpdateDto);
            updateBoardCategory(boardCategories, boardUpdateDto, board); // boardCategory 업데이트
            updateBoardImages(board, files); // Image 업데이트
        } catch (RuntimeException exception) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_CREATE_ERROR);
        }

        return new ResponseBoard(ExceptionCode.BOARD_UPDATE_OK, board.toOneBoardFindDto());
    }

    @Override
    public Object delete(Long boardId, User user) {
        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByBoard_BoardId(boardId); // validate 통일성을 위해 리스트로 참조
        try {
            validateBoard(boardCategories, user, ExceptionCode.BOARD_DELETE_INVALID);
        } catch (IllegalArgumentException exception) {
            ExceptionCode exceptionCode = ExceptionCode.findExceptionCodeByCode(exception.getMessage());
            return new ResponseBoardEmpty(exceptionCode);
        }
        Board board = boardCategories.get(0).getBoard();
        board.delete(); // status만 수정

        List<BoardImage> boardImages = board.getBoardImages();
        deleteBoardImages(boardImages);
        return new ResponseBoardEmpty(ExceptionCode.BOARD_DELETE_OK);
    }

    @Override
    public Object getOneBoard(Long boardId) {
        Board board = boardRepository.findBoardByBoardId(boardId);
        try {
            validateBoard(board);
        } catch (IllegalArgumentException exception) {
            ExceptionCode exceptionCode = ExceptionCode.findExceptionCodeByCode(exception.getMessage());
            return new ResponseBoardEmpty(exceptionCode);
        }
        return new ResponseBoard(ExceptionCode.BOARD_GET_OK, board.toOneBoardFindDto());
    }

    @Override
    @Transactional(readOnly = true)
    public Object fetchBoardPagesBy(Long lastBoardId, int size, Integer categoryId, String keyword) {
        List<Board> boards = fetchPages(lastBoardId, size, categoryId, keyword);
        List<AllBoardFindDto> allBoardFindDtos = boards.stream()
                .map((x) -> x.toAllBoardFindDto())
                .collect(Collectors.toList());
        try {
            validateBoard(boards);
        } catch (IllegalArgumentException exception) {
            ExceptionCode exceptionCode = ExceptionCode.findExceptionCodeByCode(exception.getMessage());
            return new ResponseBoardEmpty(exceptionCode);
        }

        return new ResponseBoard(ExceptionCode.BOARD_GET_OK, allBoardFindDtos);
    }

    private List<Board> fetchPages(Long lastBoardId, int size, Integer categoryId, String keyword)  {
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
            return boardCategoryRepository
                    .findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_StatusEqualsOrderByBoardDesc(lastBoardId, categoryId, 1, pageRequest)
                    .stream()
                    .map(x -> x.getBoard())
                    .collect(Collectors.toList());
        }
        // 카테고리 설정 후 검색을 진행할 때
        return boardCategoryRepository
                .findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_TitleContainingAndBoard_StatusEqualsOrderByBoardDesc(lastBoardId, categoryId, keyword,1, pageRequest)
                .stream()
                .map(x -> x.getBoard())
                .collect(Collectors.toList());
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

    /**
     * 업데이트 항목
     */
    private void updateBoardCategory(List<BoardCategory> boardCategories, BoardUpdateDto boardUpdateDto, Board board) {
        boardCategoryRepository.deleteAll(boardCategories);
        boardCategoryRepository.flush();
        boardCategories.clear();
        for (Integer i : boardUpdateDto.getCategoryId()) {
            boardCategories.add(new BoardCategory(board, categoryRepository.findByCategoryId(i)));
        }
        boardCategoryRepository.saveAllAndFlush(boardCategories);
    }

    private void updateBoardImages(Board board, List<MultipartFile> files) throws RuntimeException {
        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(board);
        deleteBoardImages(boardImages);
        try {
            boardImageRepository.saveAllAndFlush(saveBoardImage(board, files));
        } catch (Exception e) {
            System.out.println("파일을 업데이트하지 못했습니다.");
            throw new RuntimeException(e);//수정
        }
    }

    private void validateBoard(List<BoardCategory> boardCategories, User user, ExceptionCode exceptionCodeOfService) throws IllegalArgumentException {
        checkNotFound(Arrays.asList(boardCategories.toArray()));
        checkInvalidAccess(boardCategories, user, exceptionCodeOfService);
    }

    private void validateBoard(Board board) throws IllegalArgumentException { // 직접 점검 시 사용
        checkNotFound(board);
    }

    private void validateBoard(List<Board> board) throws IllegalArgumentException {
        checkNotFound(Arrays.asList(board.toArray()));
    }

    private void checkNotFound(List<Object> boardCategories) throws IllegalArgumentException {
        if (boardCategories.isEmpty()) {
            throw new IllegalArgumentException(ExceptionCode.BOARD_NOT_FOUND.getCode());
        }
    }

    private void checkNotFound(Board board) throws IllegalArgumentException {
        if (board == null) {
            throw new IllegalArgumentException(ExceptionCode.BOARD_NOT_FOUND.getCode());
        }
        if (board.getStatus() == 0) {
            throw new IllegalArgumentException(ExceptionCode.BOARD_NOT_FOUND.getCode());
        }
    }

    private void checkInvalidAccess(List<BoardCategory> boardCategories, User user, ExceptionCode exceptionCodeOfService) throws IllegalArgumentException {
        if (!boardCategories.get(0).getBoard().getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException(exceptionCodeOfService.getCode());
        }
    }
}