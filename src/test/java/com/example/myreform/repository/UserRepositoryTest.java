package com.example.myreform.repository;


import com.example.myreform.User.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @AfterEach
    private void after() {
        em.clear();
    }

    @Test
    void existsByNickname_있음() throws Exception {
        String nickname = "kong";
        System.out.println(userRepository.existsUserByNickname(nickname));

        nickname = "kung";
        System.out.println(userRepository.existsUserByNickname(nickname));
    }

}