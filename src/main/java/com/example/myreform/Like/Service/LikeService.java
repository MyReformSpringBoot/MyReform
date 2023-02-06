package com.example.myreform.Like.Service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.response.ResponseBoardEmpty;
import com.example.myreform.Like.domain.Like;
import com.example.myreform.Like.repository.LikeRepository;
import com.example.myreform.Like.response.ResponseLike;
import com.example.myreform.User.domain.User;
import com.example.myreform.validation.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;


    public Object addLike(User user, Board board){
        if(!likeRepository.existsLikeByBoard_BoardIdAndUser_UserId(board, user)){
            likeRepository.save(new Like(user, board));
            board.updateLike(true);
            return new ResponseLike(ExceptionCode.LIKE_FOUND_OK ,likeRepository.countByBoard_BoardIdAndUser_UserId(board, user));

        }
        return new ResponseLike(ExceptionCode.LIKE_DUPLICATED ,likeRepository.countByBoard_BoardIdAndUser_UserId(board, user));
    }
    public Object removeLike(User user, Board board){
        if(!likeRepository.existsLikeByBoard_BoardIdAndUser_UserId(board, user)){
            return new ResponseLike(ExceptionCode.LIKE_NOT_FOUND ,likeRepository.countByBoard_BoardIdAndUser_UserId(board, user));
        }
        board.updateLike(false);
        return new ResponseLike(ExceptionCode.LIKE_DELETE ,likeRepository.countByBoard_BoardIdAndUser_UserId(board, user));
    }

}
