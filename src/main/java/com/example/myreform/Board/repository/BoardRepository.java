package com.example.myreform.Board.repository;

import com.example.myreform.Board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    List<Board> findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(Long lastBoardId, int status, PageRequest pageRequest);
    List<Board> findAllByBoardIdLessThanAndStatusEqualsAndTitleContainingOrderByBoardIdDesc(Long lastBoardId, int status, String title, PageRequest pageRequest);
    Board findBoardByBoardId(Long boardId);

}
