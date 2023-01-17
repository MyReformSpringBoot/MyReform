package com.example.myreform.service.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostFindDto;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.repository.PostRepository;
import com.example.myreform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public Post save(User user, PostSaveDto postSaveDto) throws Exception {
        Post post = postSaveDto.toEntity();
        post.confirmUser(user);
        return postRepository.save(post);
    }

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> fetchPostPagesBy(Long lastPostId, int size) {
        Page<Post> posts = fetchPages(lastPostId, size);

        return posts.getContent();
    }

    private Page<Post> fetchPages(Long lastPageId, int size)  {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByAllPostIdOrderByPostIdDesc(lastPageId, pageRequest);
    }
}
