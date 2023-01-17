package com.example.myreform.repository;

import com.example.myreform.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{//extends JpaRepository<Post, Long>
//    public void findAllPosts();
//    public void findByKeyword();
//    public void findByCategory();
//    public Post createPost(long user_int, int categoryId, String title,String contents, String imageURL);
//    public void updatePost();
//    public void deletePost();
}
