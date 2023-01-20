package com.example.myreform.Board.repository;

import com.example.myreform.Board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    Page<Board> findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(Long lastBoardId, int status, PageRequest pageRequest);
    Page<Board> findAllByBoardIdLessThanAndStatusEqualsAndCategoryIdEqualsOrderByBoardIdDesc(Long lastBoardId, int status, Integer categoryId, PageRequest pageRequest);
    Page<Board> findAllByBoardIdLessThanAndStatusEqualsAndTitleContainingOrderByBoardIdDesc(Long lastBoardId, int status, String title, PageRequest pageRequest);
    Page<Board> findAllByBoardIdLessThanAndStatusEqualsAndCategoryIdEqualsAndTitleContainingOrderByBoardIdDesc(Long lastBoardId, int status, Integer categoryId, String title, PageRequest pageRequest);


}
