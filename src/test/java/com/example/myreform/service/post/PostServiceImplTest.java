package com.example.myreform.service.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostSaveDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class PostServiceImplTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private PostService postService;

    @Test
    public void 게시글_업로드() throws Exception {
        Long post_id = 1l;
        User user = new User(1L, "gksqlsl11@khu.ac.kr", "kong", "test", "kong", "안녕" ,"Y");
        Long category_id = 1L;
        String title = "게시글 1";
        String contents = "내용";

        PostSaveDto postSaveDto = new PostSaveDto(user, category_id, title, contents);
        postService.save(user, postSaveDto);
        em.flush();
        em.clear();
        Post findPost = em.createQuery("select p from post p", Post.class).getSingleResult();
        Post post = em.find(Post.class, findPost.getPost_id());
        assertThat(post.getTitle()).isEqualTo(title);
        System.out.println("post = " + post.getCreateAt());
    }

}