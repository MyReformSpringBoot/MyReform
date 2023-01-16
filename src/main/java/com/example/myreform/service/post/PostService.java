package com.example.myreform.service.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostSaveDto;

public interface PostService {

    // 게시글 등록
    void save(User user, PostSaveDto postSaveDto) throws Exception;
}
