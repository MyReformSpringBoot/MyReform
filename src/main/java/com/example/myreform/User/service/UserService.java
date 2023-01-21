package com.example.myreform.User.service;

import com.example.myreform.User.dto.UserLoginDto;
import com.example.myreform.User.dto.UserSignupDto;
import com.example.myreform.validation.ExceptionCode;
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
    public ResponseUser signUp(UserSignupDto signupDTO) {

        if (userRepository.findById(signupDTO.getId()).isPresent()){
            return new ResponseUser(ExceptionCode.SIGNUP_ID);
        }

        if (userRepository.findByNickname(signupDTO.getNickname()).isPresent()){
            return new ResponseUser(ExceptionCode.SIGNUP_NICKNAME);
        }
        // encode User 생성
        User user = signupDTO.toUser(passwordEncoder.encode(signupDTO.getPw()));
        userRepository.save(user);

        return new ResponseUser(ExceptionCode.SIGNUP_CREATED);
    }


    public ResponseUser login(UserLoginDto loginDTO) {

        Optional<User> user = userRepository.findById(loginDTO.getId());
        if (user.isPresent()) { // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            UserLoginDto checkIdUser = UserLoginDto.builder()
                    .id(user.get().getId())
                    .pw(user.get().getPw())
                    .build();

            if (passwordEncoder.matches(loginDTO.getPw(), checkIdUser.getPw())) { // 비밀번호 일치
                return new ResponseUser(ExceptionCode.LOGIN_OK);
            } else {
                return new ResponseUser(ExceptionCode.LOGIN_PW);
            }

        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return new ResponseUser(ExceptionCode.LOGIN_ID);
        }
    }
}
