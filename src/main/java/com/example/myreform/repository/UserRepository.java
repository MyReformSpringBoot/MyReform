package com.example.myreform.repository;

import com.example.myreform.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> existsUserByNickname(String nickname);
}
