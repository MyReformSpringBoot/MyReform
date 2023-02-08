package com.example.myreform.like;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.Like.Service.LikeService;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LikeServiceImplTest {
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void addLikeTest(){//좋아요를 안 눌렀을 때 하나 추가되는 test
        Long boardId = 37L;
        Board board = boardRepository.findById(boardId).get();
        Long prevCount = board.getCountOfLike();

        User user = userRepository.findByNickname("hil").get();
        likeService.addLike(boardId, user.getNickname());

        Long postCount = board.getCountOfLike();
        assertThat(postCount).isEqualTo(prevCount + 1);

    }
    @Test
    public void addLikeDuplicateTest(){//좋아요를 이미 누른 상태에서 하나 추가 안 되는지 확인 test
        Long boardId = 38L;
        Board board = boardRepository.findById(boardId).get();
        Long prevCount = board.getCountOfLike();

        User user = userRepository.findByNickname("hil").get();
        likeService.addLike(boardId, user.getNickname());

        Long postCount = board.getCountOfLike();
        assertThat(postCount).isEqualTo(prevCount);

    }

    @Test
    public void removeTest(){//삭제한 후 좋아요 개수 맞는 지 test
        Long boardId = 38L;
        Board board = boardRepository.findById(boardId).get();
        Long prevCount = board.getCountOfLike();

        User user = userRepository.findByNickname("hil").get();
        likeService.removeLike(boardId, user.getNickname());

        Long postCount = board.getCountOfLike();
        assertThat(postCount).isEqualTo(prevCount -1);

    }
    @Test
    public void removeNolikeTest(){//없는 좋아요 지우려고 하는 test
        Long boardId = 38L;
        Board board = boardRepository.findById(boardId).get();
        Long prevCount = board.getCountOfLike();

        User user = userRepository.findByNickname("hil").get();
        likeService.removeLike(boardId, user.getNickname());

        Long postCount = board.getCountOfLike();
        assertThat(postCount).isEqualTo(prevCount);

    }
}
