package com.example.myreform.service.board;

import com.example.myreform.controller.board.ImageUploadHandler;
import com.example.myreform.domain.board.*;
import com.example.myreform.domain.user.User;

import com.example.myreform.repository.BoardImageRepository;
import com.example.myreform.repository.BoardRepository;
import com.example.myreform.repository.ImageRepository;
import com.example.myreform.repository.UserRepository;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private final ImageRepository imageRepository;

    @Override
    public Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception {
        //post를 먼저 저장해야 postImage에 저장할 수 있음 => 따라서 save를 먼저 호출

        Board board = boardSaveDto.toEntity();
        board.confirmUser(user);
        boardRepository.save(board);
        List<BoardImage> boardImages = boardImageRepository.saveAll(saveBoardImage(board.getBoardId(), files));

        //post정보와 이미지 정보를 모두 출력하기 위해 pair사용 => key에는 post가 value에는 이미지 정보 배열이 들어있다.
        Pair<Board, List<BoardImage>> result = new Pair<>(board, boardImages);
        return result;
    }
    List<BoardImage> saveBoardImage(Long boardId, List<MultipartFile> files)throws Exception{
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
    public Object update(Long boardId, BoardSaveDto boardSaveDto, List<MultipartFile> files) {
        BoardUpdateDto boardUpdateDto = findById(boardId).toBoardUpdateDto();
        boardUpdateDto.setContents(boardSaveDto.getContents());
        boardUpdateDto.setTitle(boardSaveDto.getTitle());
        boardUpdateDto.setPrice(boardSaveDto.getPrice());
        Board board = boardUpdateDto.ToEntity(boardId);

        List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);

        boardRepository.save(board);
        try {
            boardImageRepository.saveAllAndFlush(saveBoardImage(boardId, files));
        } catch (Exception e) {
            throw new RuntimeException(e);//수정
        }
        deleteBoardImages(boardImages);
        return new Pair<>(board, boardImageRepository.findAllByBoardId(boardId));
    }

    @Override
    public String delete(Long boardId) {
        Board board;
        if (boardRepository.existsById(boardId) && findById(boardId).getStatus() != 0) {
           board = findById(boardId);

        } else {
            return "해당 게시글이 없습니다.";
        }
        BoardDeleteDto boardDeleteDto = findById(boardId).toBoardDeleteDto();
        boardDeleteDto.setStatus(0);
        board = boardDeleteDto.toEntity();

        List<BoardImage> postImages = boardImageRepository.findAllByBoardId(boardId);
        deleteBoardImages(postImages);

        boardRepository.save(board);
        return "해당 게시글을 삭제했습니다.";

    }

    @Transactional
    void deleteBoardImages(List<BoardImage> boardImages){
        List<Image> deletedImages = new ArrayList<>();
        for(BoardImage boardImage: boardImages) {
            Image image = boardImage.getImage();
            ImageDeleteDto imageDeleteDto = image.toImageDeleteDto();
            //imageDeleteDto.setStatus(0);
            image = imageDeleteDto.toEntity(boardImage.getImage().getImageId());
            deletedImages.add(image);
        }
        imageRepository.saveAll(deletedImages);
    }

    @Override
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Object fetchBoardPagesBy(Long lastBoardId, int size, Integer categoryId, String keyword) {
        Page<Board> boards = fetchPages(lastBoardId, size, categoryId, keyword);
        List<BoardFindDto> boardFindDtos = boards.getContent().stream().map((x) -> x.toDto()).collect(Collectors.toList());
        List<Pair<BoardFindDto, List<BoardImage>>> result = new ArrayList<>();
        for (BoardFindDto boardFindDto: boardFindDtos) {
            Long boardId = boardFindDto.getBoardId();
            result.add(new Pair<>(boardFindDto, boardImageRepository.findAllByBoardId(boardId)));
        }
        return result;
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