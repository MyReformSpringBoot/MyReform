package com.example.myreform.Board.service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.domain.BoardImage;
import com.example.myreform.Image.domain.Image;
import com.example.myreform.Board.dto.BoardFindDto;
import com.example.myreform.Board.dto.BoardSaveDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.Image.dto.ImageDeleteDto;
import com.example.myreform.Image.controller.ImageUploadHandler;


import com.example.myreform.Board.repository.BoardImageRepository;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Image.repository.ImageRepository;

import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
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
        user = userRepository.findById(user.getUserId()).get();
        //!!!!!(1/20)수정 사항: 넘어온 유저 유저아이디 같은 객체로 넣어줌 => createAt, updateAt이 null로 나오는 것 막기 위해!!!!!!
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
    public Object update(Long boardId, BoardSaveDto boardSaveDto, User user,List<MultipartFile> files) throws JsonProcessingException {
        if (!boardRepository.existsById(boardId) || findById(boardId).getStatus() == 0) {
            return "해당 게시글이 없습니다.";
        }

        BoardUpdateDto boardUpdateDto = findById(boardId).toBoardUpdateDto();
        System.out.println(boardUpdateDto.getCreateAt());
        if (!boardUpdateDto.getUser().getUserId().equals(user.getUserId())) {
            System.out.println(boardUpdateDto.getUser().getUserId());
            System.out.println(user.getUserId());

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("result: ", "게시물 수정 권한이 없습니다.");
            return new ObjectMapper().writeValueAsString(hashMap);
        }

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
        return new Pair<>(findById(board.getBoardId()), boardImageRepository.findAllByBoardId(boardId));
    }

    @Override
    public String delete(Long boardId, User user) {
        if (!boardRepository.existsById(boardId) || findById(boardId).getStatus() == 0) {
            return "해당 게시글이 없습니다.";
        }
        Board board = findById(boardId);
        if (!board.getUser().getUserId().equals(user.getUserId())) {
            System.out.println(board.getUser().getUserId());
            System.out.println(user.getUserId());
            return "게시물 삭제 권한이 없습니다.";
        }
        board.delete(); // status만 수정
        List<BoardImage> postImages = boardImageRepository.findAllByBoardId(boardId);
        deleteBoardImages(postImages);

        boardRepository.save(board);
        return "해당 게시글을 삭제했습니다.";
    }

    @Transactional
    void deleteBoardImages(List<BoardImage> boardImages){
        for(BoardImage boardImage: boardImages) {
            Image image = boardImage.getImage();
            //실제로 폴더에서 삭제하는 코드 => status로 진행 시 실제로 삭제 안하기에 주석처리
            String path = new File("/Users/ihyein/hil/UMC/MyReform").getAbsolutePath() + "/" + image.getImageURL();
            File file = new File(path);
            if (file.exists()) {
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