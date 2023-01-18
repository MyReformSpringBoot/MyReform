package com.example.myreform.service.board;

import com.example.myreform.controller.board.ImageUploadHandler;
import com.example.myreform.domain.board.Image;
import com.example.myreform.domain.board.Board;
import com.example.myreform.domain.user.User;

import com.example.myreform.domain.board.BoardImage;
import com.example.myreform.domain.board.BoardFindDto;
import com.example.myreform.domain.board.BoardSaveDto;
import com.example.myreform.repository.BoardImageRepository;
import com.example.myreform.repository.BoardRepository;
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

    private static final Integer MAX_CATEGORY_NUM = 5; // 카테고리 개수에 따라 수정?

    @Override
    public Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception {
        //post를 먼저 저장해야 postImage에 저장할 수 있음 => 따라서 save를 먼저 호출
        //이후 생성된 db에 이미 저장된, 방금 만든 post반환을 위해 findById를 호출함
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
    public Board update(Long boardId, BoardSaveDto boardSaveDto, List<MultipartFile> files) {
        Board board = findById(boardId);
        board.updateContents(boardSaveDto.getContents());
        board.updateTitle(boardSaveDto.getTitle());

        List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);

        boardRepository.save(board);
        try {
            boardImageRepository.saveAllAndFlush(saveBoardImage(boardId, files));
        } catch (Exception e) {
            throw new RuntimeException(e);//수정
        }
        deleteBoardImages(boardImages);
        return board;
    }


    @Override
    public String delete(Long boardId) {
        Board board;
        if (boardRepository.existsById(boardId)) {
           board = findById(boardId);
        } else {
            return "해당 게시글이 없습니다.";
        }

        List<BoardImage> postImages = boardImageRepository.findAllByBoardId(boardId);
        deleteBoardImages(postImages);

        boardRepository.delete(board);
        return "해당 게시글을 삭제했습니다.";

    }

    @Transactional
    void deleteBoardImages(List<BoardImage> boardImages){
        for(BoardImage boardImage: boardImages){
            Image image =  boardImage.getImage();

            String path = new File("/Users/ihyein/hil/UMC/MyReform").getAbsolutePath() + "/" + image.getImageURL();
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
        }
        boardImageRepository.deleteAll(boardImages);
    }

    @Override
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Object fetchBoardPagesBy(Long lastBoardId, int size) {
        Page<Board> boards = fetchPages(lastBoardId, size);
        List<BoardFindDto> boardFindDtos = boards.getContent().stream().map((x) -> x.toDto()).collect(Collectors.toList());
        List<Pair<BoardFindDto, List<BoardImage>>> result = new ArrayList<>();
        for (BoardFindDto boardFindDto: boardFindDtos) {
            Long boardId = boardFindDto.getBoardId();
            result.add(new Pair<>(boardFindDto, boardImageRepository.findAllByBoardId(boardId)));
        }
        return result;
    }

    private Page<Board> fetchPages(Long lastBoardId, int size)  {
        System.out.println("size = " + size);
        System.out.println("lastPageId = " + lastBoardId);

        PageRequest pageRequest = PageRequest.of(0, size);
        return boardRepository.findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(lastBoardId, 1, pageRequest);
    }
}
