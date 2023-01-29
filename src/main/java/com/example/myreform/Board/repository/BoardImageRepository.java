package com.example.myreform.Board.repository;


import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findAllByBoard(Board board);

}
