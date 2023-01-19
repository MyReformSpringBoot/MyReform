package com.example.myreform.repository;

import com.example.myreform.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(Long lastBoardId, int status, PageRequest pageRequest);

    Page<Board> findAllByBoardIdLessThanAndStatusEqualsAndCategoryIdEqualsOrderByBoardIdDesc(Long lastBoardId, int status, Integer categoryId, PageRequest pageRequest);
}
