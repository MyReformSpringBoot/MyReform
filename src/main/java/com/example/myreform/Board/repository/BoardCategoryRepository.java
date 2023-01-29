package com.example.myreform.Board.repository;

import com.example.myreform.Board.domain.BoardCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    List<BoardCategory> findAllByBoard_BoardId(Long boardId);
    List<BoardCategory> findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_StatusEqualsOrderByBoardDesc(Long lastBoardId, Integer categoryId, int status, PageRequest pageRequest);
    List<BoardCategory> findAllByBoard_BoardIdLessThanAndCategory_CategoryIdEqualsAndBoard_TitleContainingAndBoard_StatusEqualsOrderByBoardDesc(Long lastBoardId, Integer categoryId, String keyword, int status, PageRequest pageRequest);
}
