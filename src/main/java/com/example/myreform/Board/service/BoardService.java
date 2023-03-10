package com.example.myreform.Board.service;

import com.example.myreform.Board.domain.Board;

import com.example.myreform.Board.dto.BoardSaveDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.User.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    // 게시글 등록
    Object save(BoardSaveDto boardSaveDto, List<MultipartFile> files) throws Exception;
    Object update(Long boardId, BoardUpdateDto boardUpdateDto, List<MultipartFile> files) throws JsonProcessingException;
    Object delete(Long boardId, User user);
    Object getOneBoard(Long boardId, String loginId);
    Object fetchBoardPagesBy(Long lastBoardId, int size, Integer categoryId, String keyword, String loginId);

    Object checkAuthority(Long boardId, String loginId);
}
