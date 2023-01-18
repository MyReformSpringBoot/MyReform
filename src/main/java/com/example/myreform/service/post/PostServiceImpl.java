package com.example.myreform.service.post;

import com.example.myreform.controller.post.ImageUploadHandler;
import com.example.myreform.domain.Image;
import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;

import com.example.myreform.domain.PostImage;
import com.example.myreform.model.post.PostFindDto;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.repository.PostImageRepository;
import com.example.myreform.repository.PostRepository;
import com.example.myreform.repository.UserRepository;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ImageUploadHandler imageUploadHandler;
    @Autowired
    private final PostImageRepository postImageRepository;

    private static final Integer MAX_CATEGORY_NUM = 5; // 카테고리 개수에 따라 수정?

    @Override
    public Object save(User user, PostSaveDto postSaveDto, List<MultipartFile> files) throws Exception {
        //post를 먼저 저장해야 postImage에 저장할 수 있음 => 따라서 save를 먼저 호출
        //이후 생성된 db에 이미 저장된, 방금 만든 post반환을 위해 findById를 호출함
        Post post = postSaveDto.toEntity();
        post.confirmUser(user);
        postRepository.save(post);

        List<PostImage> postImages = postImageRepository.saveAll(savePostImage(post.getPostId(), files));

        //post정보와 이미지 정보를 모두 출력하기 위해 pair사용 => key에는 post가 value에는 이미지 정보 배열이 들어있다.
        Pair<Post, List<PostImage>> result = new Pair<>(post, postImages);
        return result;
    }
    List<PostImage> savePostImage(Long postId, List<MultipartFile> files)throws Exception{
        List<Image> imageList = imageUploadHandler.parseImageInfo(postId, files);

        List<PostImage> postImages = new ArrayList<>();
        for(Image image : imageList){
            PostImage postImage = PostImage.builder()
                    .image(image)
                    .postId(postId)
                    .build();
            postImages.add(postImage);
        }
        return postImages;
    }

    @Override
    public Post update(Long postId, PostSaveDto postSaveDto, List<MultipartFile> files) {
        Post post = findById(postId);
        post.updateContents(postSaveDto.getContents());
        post.updateTitle(postSaveDto.getTitle());

        List<PostImage> postImages = postImageRepository.findAllByPostId(postId);

        postRepository.save(post);
        try {
            postImageRepository.saveAllAndFlush(savePostImage(postId, files));
        } catch (Exception e) {
            throw new RuntimeException(e);//수정
        }
        deletePostImages(postImages);
        return post;
    }


    @Override
    public String delete(Long postId) {
        Post post;
        if (postRepository.existsById(postId)) {
           post = findById(postId);
        } else {
            return "해당 게시글이 없습니다.";
        }

        List<PostImage> postImages = postImageRepository.findAllByPostId(postId);
        deletePostImages(postImages);

        postRepository.delete(post);
        return "해당 게시글을 삭제했습니다.";

    }

    @Transactional
    void deletePostImages(List<PostImage> postImages){
        for(PostImage postImage: postImages){
            Image image =  postImage.getImage();

            String path = new File("/Users/ihyein/hil/UMC/MyReform").getAbsolutePath() + "/" + image.getImageURL();
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
        }
        postImageRepository.deleteAll(postImages);
    }

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostFindDto> fetchPostPagesBy(Long lastPostId, int size) {
        Page<Post> posts = fetchPages(lastPostId, size);
        System.out.println("posts = " + posts.getTotalPages());
        System.out.println("posts = " + posts.getContent());
        System.out.println(posts.getContent().stream().map((x) -> x.toDto()).collect(Collectors.toList()).get(0));
        return posts.getContent().stream().map((x) -> x.toDto()).collect(Collectors.toList());
    }

    private Page<Post> fetchPages(Long lastPageId, int size)  {
        System.out.println("size = " + size);
        System.out.println("lastPageId = " + lastPageId);

        PageRequest pageRequest = PageRequest.of(0, size);
        System.out.println(postRepository.findAllByPostIdLessThanOrderByPostIdDesc(lastPageId, pageRequest).getContent().get(0));
        return postRepository.findAllByPostIdLessThanOrderByPostIdDesc(lastPageId, pageRequest);
    }
}
