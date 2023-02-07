package com.example.myreform.Like.repository;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Like.domain.Like;
import com.example.myreform.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {//이후에 nickname => token으로
    boolean existsLikeByBoard_BoardIdAndUser_Nickname(Long boardId, String nickname);

    Long countByBoard_BoardIdAndUser_Nickname(Long boardId, String nickname);
    Long countByBoard_BoardId(Long boardId);
    Long countByUser_Nickname(String nickname);

    Like findByBoard_BoardIdAndUser_Nickname(Long boardId, String nickname);
    List<Like> findAllByUser_Nickname(String nickname);
}
