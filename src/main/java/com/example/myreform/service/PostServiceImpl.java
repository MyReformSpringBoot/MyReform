package com.example.myreform.service;

import com.example.myreform.controller.ImageHandler;
import com.example.myreform.model.Image;
import com.example.myreform.model.Post;
import com.example.myreform.model.PostImage;
import com.example.myreform.repository.ImageRepository;
import com.example.myreform.repository.PostImageRepository;
import com.example.myreform.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl {
    private final PostRepository postRepository;
    private final ImageHandler imageHandler;
    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;

    //@Autowired
    public PostServiceImpl(PostRepository postRepository, ImageHandler imageHandler, ImageRepository imageRepository, PostImageRepository postImageRepository) {
        this.postRepository = postRepository;
        this.imageHandler = imageHandler;
        this.imageRepository = imageRepository;
        this.postImageRepository = postImageRepository;
    }

    public Post createPost(long userId, int categoryId, String title,String contents, List<MultipartFile> files) throws Exception {

        Post post = Post.builder()
                .userId(userId)
                .categoryId(categoryId)
                .title(title)
                .contents(contents)
                .build();

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

        return post;
    }

//    @Transactional
//    public Optional<Post> updatePost(long postId){
//        Optional<Post> post = Optional.ofNullable(postRepository.findById(postId).orElseThrow());//
//        if(post.isPresent()){
//            postRepository.save(post.get());
//            postImageRepository.saveAll(postImageRepository.findAllByPostId(postId));
//            imageRepository.saveAll(imageRepository.findAllByPostId(postId));
//        }
//
//
//        return post;
//    }
//
//    public void deletePost(long postId){
//        postRepository.deleteById(postId);
//        postImageRepository.deleteAll(postImageRepository.findAllByPostId(postId));
//        imageRepository.deleteAll(imageRepository.findAllByPostId(postId));
//    }
}
