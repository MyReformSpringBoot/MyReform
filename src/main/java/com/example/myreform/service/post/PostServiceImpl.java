package com.example.myreform.service.post;

import com.example.myreform.controller.post.ImageHandler;
import com.example.myreform.domain.Image;
import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;

import com.example.myreform.domain.PostImage;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.repository.ImageRepository;
import com.example.myreform.repository.PostImageRepository;
import com.example.myreform.repository.PostRepository;
import com.example.myreform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ImageHandler imageHandler;
    @Autowired
    private final ImageRepository imageRepository;
    @Autowired
    private final PostImageRepository postImageRepository;

    @Override
    public Post save(User user, PostSaveDto postSaveDto, List<MultipartFile> files) throws Exception {
        //!!!!! imgae JOIN(Column) 전 !!!!!
        //post를 먼저 저장해야 postImage에 저장할 수 있음 => 따라서 save를 먼저 호출
        //이후 생성된 db에 이미 저장된, 방금 만든 post반환을 위해 findById를 호출함
        Post post = postSaveDto.toEntity();
        post.confirmUser(user);
        postRepository.save(post);

        List<Image> imageList = imageHandler.parseImageInfo(post.getPostId(), files);

        List<PostImage> postImages = new ArrayList<>();
        for(Image image : imageList){
            imageRepository.save(image);

            PostImage postImage = PostImage.builder()
                    .imageId(image.getImageId())
                    .postId(post.getPostId())
                    .build();
            postImages.add(postImage);

        }
        postImageRepository.saveAll(postImages);

        return findById(post.getPostId());
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
        return postRepository.findByPostIdOrderByPostIdDesc(lastPageId, pageRequest);
    }
}
