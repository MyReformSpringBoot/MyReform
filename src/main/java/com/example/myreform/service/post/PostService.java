package com.example.myreform.service.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostFindDto;
import com.example.myreform.model.post.PostSaveDto;

import java.util.Optional;

public interface PostService {

    // 게시글 등록
    Post save(User user, PostSaveDto postSaveDto) throws Exception;

    Post findById(Long postId);
}
