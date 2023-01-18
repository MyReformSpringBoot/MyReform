package com.example.myreform.service.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostFindDto;
import com.example.myreform.model.post.PostSaveDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    // 게시글 등록
    Object save(User user, PostSaveDto postSaveDto, List<MultipartFile> files) throws Exception;

    Object update(Long postId,PostSaveDto postSaveDto, List<MultipartFile> files);

    void delete(Long postId);

    Post findById(Long postId);

    List<PostFindDto> fetchPostPagesBy(Long lastPostId, int size);
}
