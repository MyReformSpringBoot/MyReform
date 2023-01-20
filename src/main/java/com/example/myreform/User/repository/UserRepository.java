package com.example.myreform.User.repository;

import com.example.myreform.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
    Optional<User> findByNickname(String nickname);
    Optional<User> existsUserByNickname(String nickname);
}
