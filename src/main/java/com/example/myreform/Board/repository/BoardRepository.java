package com.example.myreform.Board.repository;

import com.example.myreform.Board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    Page<Board> findAllByBoardIdLessThanAndStatusEqualsOrderByBoardIdDesc(Long lastBoardId, int status, PageRequest pageRequest);
    Page<Board> findAllByBoardIdLessThanAndStatusEqualsAndTitleContainingOrderByBoardIdDesc(Long lastBoardId, int status, String title, PageRequest pageRequest);
    Page<Board> findAllByBoardIdLessThanAndBoardIdInOrderByBoardIdDesc(Long lastBoardId, List<Long> boards, PageRequest pageRequest);
    Board findBoardByBoardId(Long boardId);

    //Page<Board> findAllByBoardIdInOrderByBoardIdDesc(List<Long> boards, PageRequest pageRequest);
}
