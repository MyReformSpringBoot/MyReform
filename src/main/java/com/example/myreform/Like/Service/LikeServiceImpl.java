package com.example.myreform.Like.Service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Like.domain.Like;
import com.example.myreform.Like.repository.LikeRepository;
import com.example.myreform.Like.response.ResponseLike;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.User.response.ResponseUser;
import com.example.myreform.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService{
    @Autowired
    private final LikeRepository likeRepository;
    @Autowired
    private final BoardRepository boardRepository;
    @Autowired
    private final UserRepository userRepository;
    @Override
    public Object addLike(Long boardId, String nickname){
        if(!likeRepository.existsLikeByBoard_BoardIdAndUser_Nickname(boardId, nickname)){
            Board board = boardRepository.findById(boardId).get();
            User user = userRepository.findByNickname(nickname).get();
            likeRepository.save(new Like(user, board));
            board.updateLike(true);
            return new ResponseLike(ExceptionCode.LIKE_FOUND_OK ,likeRepository.countByBoard_BoardId(boardId));

        }
        return new ResponseLike(ExceptionCode.LIKE_DUPLICATED ,likeRepository.countByBoard_BoardId(boardId));
    }
    @Override
    public Object removeLike(Long boardId, String nickname){
        if(!likeRepository.existsLikeByBoard_BoardIdAndUser_Nickname(boardId, nickname)){
            return new ResponseLike(ExceptionCode.LIKE_NOT_FOUND ,likeRepository.countByBoard_BoardId(boardId));
        }
        Board board = boardRepository.findById(boardId).get();
        board.updateLike(false);
        if(!userRepository.existsByNickname(nickname)){
            return new ResponseUser(ExceptionCode.USER_NOT_FOUND);
        }
        userRepository.findByNickname(nickname).get().getLikes().remove(this);
        likeRepository.delete(likeRepository.findByBoard_BoardIdAndUser_Nickname(boardId, nickname));

        return new ResponseLike(ExceptionCode.LIKE_DELETE ,likeRepository.countByBoard_BoardId(boardId));
    }

}
