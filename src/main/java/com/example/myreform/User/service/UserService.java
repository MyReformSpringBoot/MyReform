package com.example.myreform.User.service;

import com.example.myreform.User.dto.LoginDTO;
import com.example.myreform.User.dto.SignupDTO;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.response.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public ResponseUser signUp(SignupDTO signupDTO) {
        ResponseUser response;

        response = validateDuplicateUser(signupDTO);
        if (response.getStatus() == 409) {
            return response;
        }

        // encode User 생성
        User user = signupDTO.toUser(passwordEncoder.encode(signupDTO.getPw()));

        userRepository.save(user);

        response.setStatus(201);
        response.setDescription("회원가입성공");
        return response;
    }

    private ResponseUser validateDuplicateUser(SignupDTO signupDTO) {
        ResponseUser responseUser = new ResponseUser();
        String description;

        if (userRepository.findById(signupDTO.getId()).isPresent()){
            responseUser.setStatus(409);
            responseUser.setCode("A001");
            responseUser.setDescription("중복 id");
            return responseUser;
        }
        
        if (userRepository.findByNickname(signupDTO.getNickname()).isPresent()){
            responseUser.setStatus(409);
            responseUser.setCode("A002");
            responseUser.setDescription("중복 nickname");
            return responseUser;
        }

        return responseUser;
    }


    public ResponseUser login(LoginDTO loginDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */

        ResponseUser responseUser = new ResponseUser();

        Optional<User> user = userRepository.findById(loginDTO.getId());
        if (user.isPresent()) { // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            LoginDTO checkIdUser = LoginDTO.builder()
                    .id(user.get().getId())
                    .pw(user.get().getPw())
                    .build();

            if (passwordEncoder.matches(loginDTO.getPw(), checkIdUser.getPw())) { // 비밀번호 일치
                responseUser.setStatus(200);
                responseUser.setDescription("로그인 성공");
            } else {
                responseUser.setStatus(404);
                responseUser.setCode("B001");
                responseUser.setDescription("로그인 실패 - 비밀번호 틀림");
            }
            return responseUser;

        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            responseUser.setStatus(404);
            responseUser.setCode("B002");
            responseUser.setDescription("로그인 실패 - 아이디 틀림");
            return responseUser;
        }
    }
}
