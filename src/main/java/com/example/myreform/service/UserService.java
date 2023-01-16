package com.example.myreform.service;

import com.example.myreform.domain.User;
import com.example.myreform.domain.UserDTO;
import com.example.myreform.domain.UserResponse;
import com.example.myreform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse signUp(UserDTO userDTO) {
        UserResponse response;

        response = validateDuplicateUser(userDTO);
        if (response.getCode() == 400) {
            return response;
        }

        // encode User 생성
        //User user = userDTO.toUser(passwordEncoder.encode(userDTO.getPw()));
        User user = userDTO.toUser(userDTO.getPw());
        userRepository.save(user);

        response.setCode(200);
        response.setDescription("성공");
        return response;
    }

    private UserResponse validateDuplicateUser(UserDTO userDTO) {
        UserResponse userResponse = new UserResponse();
        
        String description;
        if (userRepository.findById(userDTO.getId()).isPresent()){
            // throw new Exception("존재하는 id");
            userResponse.setCode(400);
            userResponse.setDescription("중복 id");
        }
        if (userRepository.findByNickname(userDTO.getNickname()).isPresent()){
            //throw new Exception("존재하는 닉네임");
            if (userResponse.getCode() != 400) {
                userResponse.setCode(400);
                userResponse.setDescription("중복 nickname");
            }
            else {
                description = userResponse.getDescription();
                userResponse.setDescription(description + " & 중복 nickname");
            }
        }
        
        if (userResponse.getCode() == 400) {
            return userResponse;
        }

        userResponse.setCode(200);
        return userResponse;
    }

    public UserResponse login(UserDTO userDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */

        UserResponse userResponse = new UserResponse();

        Optional<User> user = userRepository.findById(userDTO.getId());
        if (user.isPresent()) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            UserDTO checkIdUser = UserDTO.builder()
                    .id(user.get().getId())
                    .email(user.get().getEmail())
                    .nickname(user.get().getNickname())
                    .pw(user.get().getPw())
                    .build();

            if (checkIdUser.getPw().equals(userDTO.getPw())) {
                // 비밀번호 일치
                userResponse.setCode(200);
                userResponse.setDescription("로그인 성공");
                userResponse.setData(checkIdUser);
                return userResponse;
            } else {
                userResponse.setCode(400);
                userResponse.setDescription("비밀번호 틀림");
                return userResponse;
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            userResponse.setCode(400);
            userResponse.setDescription("아이디 틀림");
            return userResponse;
        }
    }
}
