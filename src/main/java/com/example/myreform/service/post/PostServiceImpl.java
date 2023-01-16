package com.example.myreform.service.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.repository.PostRepository;
import com.example.myreform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public void save(User user, PostSaveDto postSaveDto) throws Exception {
        Post post = postSaveDto.toEntity();
//        post.confirmUser();
        System.out.println("post.getContents() = " + post.getContents());
        System.out.println("post = " + post.getPost_id());
        post.confirmUser(user);
        postRepository.save(post);
    }
}
