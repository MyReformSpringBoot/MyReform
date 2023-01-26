package com.example.myreform.Board.repository;

import com.example.myreform.Board.domain.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    List<BoardCategory> findAllByBoard_BoardId(Long boardId);
}
