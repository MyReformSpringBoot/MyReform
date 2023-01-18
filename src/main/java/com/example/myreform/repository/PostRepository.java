package com.example.myreform.repository;

import com.example.myreform.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByPostIdOrderByPostIdDesc(Long lastPostId, PageRequest pageRequest);


}
