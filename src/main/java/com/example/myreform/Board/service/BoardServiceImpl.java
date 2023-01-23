package com.example.myreform.Board.service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.domain.BoardImage;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.response.ResponseBoardEmpty;
import com.example.myreform.Image.domain.Image;
import com.example.myreform.Board.dto.BoardFindDto;
import com.example.myreform.Board.dto.BoardSaveDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.Image.controller.ImageUploadHandler;


import com.example.myreform.Board.repository.BoardImageRepository;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Image.repository.ImageRepository;

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

    @Value("${img.path}")
    private String IMG_PATH;

    @Override
    public Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception {
        //post를 먼저 저장해야 postImage에 저장할 수 있음 => 따라서 save를 먼저 호출
        Board board = boardSaveDto.toEntity();
        user = userRepository.findById(user.getUserId()).get();
        //!!!!!(1/20)수정 사항: 넘어온 유저 유저아이디 같은 객체로 넣어줌 => createAt, updateAt이 null로 나오는 것 막기 위해!!!!!!
        board.confirmUser(user);
        boardRepository.save(board);
        List<BoardImage> boardImages = boardImageRepository.saveAll(saveBoardImage(board.getBoardId(), files));

        //post정보와 이미지 정보를 모두 출력하기 위해 pair사용 => key에는 post가 value에는 이미지 정보 배열이 들어있다.
        Pair<BoardFindDto, List<BoardImage>> data = new Pair<>(board.toFindDto(), boardImages);
        return new ResponseBoard(ExceptionCode.BOARD_CREATE_OK, data);
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

    @Override
    public Object update(Long boardId, BoardUpdateDto boardUpdateDto, User user, List<MultipartFile> files) throws JsonProcessingException {

        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty() || boardOptional.get().getStatus() == 0) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_NOT_FOUND);
        }
        if (!boardOptional.get().getUser().getUserId().equals(user.getUserId())) {
            return new ResponseBoardEmpty(ExceptionCode.BOARD_UPDATE_INVALID);
        }

        LocalDateTime createAt = boardOptional.get().getCreateAt();
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
        BoardFindDto boardFindDto = boardRepository.findBoardByBoardId(boardId).toFindDto();
        boardFindDto.setCreateAt(createAt);
        boardImages = boardImageRepository.findAllByBoardId(boardId);
        Object data = new Pair<>(boardFindDto, boardImages);

        return new ResponseBoard(ExceptionCode.BOARD_UPDATE_OK, data);
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
        List<BoardFindDto> boardFindDtos = boards.getContent().stream().map((x) -> x.toFindDto()).collect(Collectors.toList());
        List<Pair<BoardFindDto, List<BoardImage>>> data = new ArrayList<>();
        ExceptionCode exceptionCode = ExceptionCode.BOARD_GET_OK;
        if (boardFindDtos.isEmpty()) {
            exceptionCode = ExceptionCode.BOARD_NOT_FOUND;
            return new ResponseBoardEmpty(exceptionCode);
        }
        for (BoardFindDto boardFindDto: boardFindDtos) {
            Long boardId = boardFindDto.getBoardId();
            data.add(new Pair<>(boardFindDto, boardImageRepository.findAllByBoardId(boardId)));
        }
        return new ResponseBoard(exceptionCode, data);
    }

    private Page<Board> fetchPages(Long lastBoardId, int size, Integer categoryId, String keyword)  {
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
}