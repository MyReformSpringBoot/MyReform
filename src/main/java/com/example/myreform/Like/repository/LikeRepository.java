package com.example.myreform.Like.repository;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Like.domain.Like;
import com.example.myreform.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsLikeByBoard_BoardIdAndUser_UserId(Board board, User user);
    Long countByBoard_BoardIdAndUser_UserId(Board board, User user);
    Like findByBoard_BoardIdAndUser_UserId(Board board, User user);
    List<Like> findAllByUser_UserId(User user);
}
