package com.example.myreform.service.board;

import com.example.myreform.domain.board.Board;
import com.example.myreform.domain.user.User;
import com.example.myreform.domain.board.BoardFindDto;
import com.example.myreform.domain.board.BoardSaveDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    // 게시글 등록
    Object save(User user, BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception;

    Object update(Long boardId, BoardSaveDto boardSaveDto, List<MultipartFile> files);

    String delete(Long boardId);

    Board findById(Long boardId);

    Object fetchBoardPagesBy(Long lastBoardId, int size);
}