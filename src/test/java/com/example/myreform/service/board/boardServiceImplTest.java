package com.example.myreform.service.board;

import com.example.myreform.domain.board.Board;
import com.example.myreform.domain.user.User;
import com.example.myreform.domain.board.BoardSaveDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class boardServiceImplTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private BoardService boardService;

    @Test
    public void 게시글_업로드() throws Exception {
        Long post_id = 1l;
        User user = new User(1L, "gksqlsl11@khu.ac.kr", "kong", "test", "kong", "안녕" ,"Y");
        Integer category_id = 1;
        String title = "게시글 1";
        String contents = "내용";

        BoardSaveDto postSaveDto = new BoardSaveDto(user, category_id, title, contents);
        //이미지 어떻게 테스트 케이스로 넣어야할지 몰라서 주석처리함 => postman으로 테스트 진행함
        //postService.save(user, postSaveDto, );
        em.flush();
        em.clear();
        Board findPost = em.createQuery("select p from board p", Board.class).getSingleResult();
        Board board = em.find(Board.class, findPost.getBoardId());
        assertThat(board.getTitle()).isEqualTo(title);
        System.out.println("post = " + board.getCreateAt());
    }

}