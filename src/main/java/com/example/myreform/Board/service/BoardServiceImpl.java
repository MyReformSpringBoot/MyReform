package com.example.myreform.Board.service;

import com.example.myreform.Board.category.CategoryRepository;
import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.domain.BoardCategory;
import com.example.myreform.Board.domain.BoardImage;
import com.example.myreform.Board.dto.*;
import com.example.myreform.Board.repository.BoardCategoryRepository;
import com.example.myreform.Board.response.ResponseAuthority;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.response.ResponseBoardEmpty;
import com.example.myreform.Image.domain.Image;
import com.example.myreform.Image.controller.ImageUploadHandler;


import com.example.myreform.Board.repository.BoardImageRepository;
import com.example.myreform.Board.repository.BoardRepository;

import com.example.myreform.Like.repository.LikeRepository;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.validation.ExceptionCode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private final LikeRepository likRepository;

    @Value("${img.path}")
    private String IMG_PATH;

    private static final Integer STATUS = 1;

    @Override
    public Object save(BoardSaveDto boardSaveDto, List<MultipartFile> files) {

        User user = userRepository.findById(boardSaveDto.getId()).get(); // db에 저장된 객체와의 연관관계 부여를 위해 find 진행
        Board board = boardSaveDto.toEntity(user);
        try {
            List<BoardCategory> boardCategories = new ArrayList<>();
            for (Integer categoryId : boardSaveDto.getCategoryId()) {
                boardCategories.add(new BoardCategory(board, categoryRepository.findByCategoryId(categoryId)));
            }
            boardCategoryRepository.saveAll(boardCategories);
            boardImageRepository.saveAll(saveBoardImage(board, files));
        } catch (Exception exception) {
            System.out.println(exception);
            return new ResponseBoardEmpty(ExceptionCode.BOARD_CREATE_ERROR);
        }

        return new ResponseBoardEmpty(ExceptionCode.BOARD_CREATE_OK);
    }

    @Override
    public Object update(Long boardId, BoardUpdateDto boardUpdateDto, List<MultipartFile> files) {

        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByBoard_BoardIdAndBoard_Status(boardId, STATUS);
        try {
            validateBoard(boardCategories, boardUpdateDto.getId(), ExceptionCode.BOARD_UPDATE_INVALID);
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

        return new ResponseBoardEmpty(ExceptionCode.BOARD_UPDATE_OK);
    }

    @Override
    public Object delete(Long boardId, User user) {
        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByBoard_BoardIdAndBoard_Status(boardId, STATUS); // validate 통일성을 위해 리스트로 참조
        try {
            validateBoard(boardCategories, user.getId(), ExceptionCode.BOARD_DELETE_INVALID);
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
    public Object getOneBoard(Long boardId, String loginId) {
        Board board = boardRepository.findBoardByBoardIdAndStatusEquals(boardId, STATUS);
        try {
            validateBoard(board);
        } catch (IllegalArgumentException exception) {
            ExceptionCode exceptionCode = ExceptionCode.findExceptionCodeByCode(exception.getMessage());
            return new ResponseBoardEmpty(exceptionCode);
        }
        OneBoardFindDto oneBoardFindDto = board.toOneBoardFindDto(); // 하나의 게시글에서 좋아요 보여주기
        oneBoardFindDto.setLikeOrNot(likRepository.existsLikeByBoard_BoardIdAndUser_Id(boardId, loginId));
        return new ResponseBoard(ExceptionCode.BOARD_GET_OK, oneBoardFindDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Object fetchBoardPagesBy(Long lastBoardId, int size, Integer categoryId, String keyword, String loginId) {
        List<Board> boards = fetchPages(lastBoardId, size, categoryId, keyword);
        List<AllBoardFindDto> allBoardFindDtos = boards.stream()
                .map((x) -> x.toAllBoardFindDto())
                .collect(Collectors.toList());
        allBoardFindDtos.forEach(x->x.setLikeOrNot(likRepository.existsLikeByBoard_BoardIdAndUser_Id(x.getBoardId(), loginId)));

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
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(lastBoardId, STATUS, pageRequest);
        }
        if (Optional.ofNullable(categoryId).isEmpty()) { // 모든 카테고리에 대해 검색만 할 때
            return boardRepository.findAllByBoardIdLessThanAndStatusEqualsAndTitleContainingOrderByBoardIdDesc(lastBoardId, STATUS, keyword, pageRequest);
        }
        if (keyword == null) { // 검색을 안하고 카테고리만 찾아볼 때
            return boardCategoryRepository
                    .findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_StatusEqualsOrderByBoardDesc(lastBoardId, categoryId, STATUS, pageRequest)
                    .stream()
                    .map(x -> x.getBoard())
                    .collect(Collectors.toList());
        }
        // 카테고리 설정 후 검색을 진행할 때
        return boardCategoryRepository
                .findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_TitleContainingAndBoard_StatusEqualsOrderByBoardDesc(lastBoardId, categoryId, keyword,STATUS, pageRequest)
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
        System.out.println("files.get(0) = " + files.get(0));
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
    @Override
    public Object checkAuthority(Long boardId, String loginId){
        Board board;
        try{
            board = boardRepository.findById(boardId).get();
        } catch (Exception e){
            return new ResponseBoardEmpty(ExceptionCode.BOARD_NOT_FOUND);
        }

        if(loginId.equals(board.getUser().getId())){
            return new ResponseAuthority(ExceptionCode.AUTHORITY_HAVE, true);
        }
        return new ResponseAuthority(ExceptionCode.AUTHORITY_NOT_HAVE, false);
    }

    private void validateBoard(List<BoardCategory> boardCategories, String id, ExceptionCode exceptionCodeOfService) throws IllegalArgumentException {
        checkNotFound(boardCategories);
        checkInvalidAccess(boardCategories, id, exceptionCodeOfService);
    }

    private void validateBoard(Board board) throws IllegalArgumentException { // 직접 점검 시 사용
        checkNotFound(board);
    }

    private void validateBoard(List<Board> board) throws IllegalArgumentException {
        checkNotFound(board);
    }

    private void checkNotFound(Object board) throws IllegalArgumentException {
        if (board == null || board.toString().equals("[]")) {
            throw new IllegalArgumentException(ExceptionCode.BOARD_NOT_FOUND.getCode());
        }
    }

    private void checkInvalidAccess(List<BoardCategory> boardCategories, String id, ExceptionCode exceptionCodeOfService) throws IllegalArgumentException {
        if (!boardCategories.get(0).getBoard().getUser().getId().equals(id)) {
            throw new IllegalArgumentException(exceptionCodeOfService.getCode());
        }
    }
}