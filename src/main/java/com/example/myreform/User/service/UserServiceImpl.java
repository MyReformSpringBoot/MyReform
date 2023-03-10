package com.example.myreform.User.service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.User.dto.UserFindDto;
import com.example.myreform.User.dto.UserLoginDto;
import com.example.myreform.User.dto.UserSignupDto;
import com.example.myreform.User.dto.UserUpdateDto;
import com.example.myreform.User.response.ResponseProfile;
import com.example.myreform.validation.ExceptionCode;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.response.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Object signUp(UserSignupDto signupDTO) {

        if (userRepository.findById(signupDTO.getId()).isPresent()){
            return new ResponseUser(ExceptionCode.SIGNUP_DUPLICATED_ID);
        }

        if (userRepository.findByNickname(signupDTO.getNickname()).isPresent()){
            return new ResponseUser(ExceptionCode.SIGNUP_DUPLICATED_NICKNAME);
        }
        // encode User 생성
        User user = signupDTO.toUser(passwordEncoder.encode(signupDTO.getPw()));
        userRepository.save(user);

        return new ResponseUser(ExceptionCode.SIGNUP_CREATED_OK);
    }


    public Object login(UserLoginDto loginDTO) {

        Optional<User> user = userRepository.findById(loginDTO.getId());
        if (user.isPresent()) { // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            UserLoginDto checkIdUser = UserLoginDto.builder()
                    .id(user.get().getId())
                    .pw(user.get().getPw())
                    .build();
            if (passwordEncoder.matches(loginDTO.getPw(), checkIdUser.getPw())) { // 비밀번호 일치
                String token = user.get().getId();
                return new ResponseUser(ExceptionCode.LOGIN_OK, token);
            }
            return new ResponseUser(ExceptionCode.LOGIN_NOT_FOUND_PW);
        }
        // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
        return new ResponseUser(ExceptionCode.LOGIN_NOT_FOUND_ID);
    }


    @Override
    public Object find(String loginId) {
        Optional<User> userOp = userRepository.findById(loginId);
        if(userOp.isEmpty()){
            return new ResponseUser(ExceptionCode.USER_NOT_FOUND);
        }
        User user= userOp.get();

        UserFindDto findDto = user.toFindDto();
        return new ResponseProfile(ExceptionCode.USER_GET_OK, findDto);

    }

    @Override
    public Object update(String loginId, UserUpdateDto userUpdateDto) {

        String AlterNickname = userUpdateDto.getNickname();
        if (!isPresentLoginId(loginId)){
            return new ResponseUser(ExceptionCode.USER_NOT_FOUND);
        }

        User user = userRepository.findById(loginId).get();
        if (isPresentNickname(AlterNickname) && !AlterNickname.equals(user.getNickname())) {
            return new ResponseUser(ExceptionCode.SIGNUP_DUPLICATED_NICKNAME);
        }

        try {
            user.update(userUpdateDto);
        } catch (RuntimeException exception) {
            return new ResponseUser(ExceptionCode.USER_UPDATE_INVALID);
        }
        return new ResponseProfile(ExceptionCode.USER_UPDATE_OK);
    }

    private boolean isPresentNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
    private boolean isPresentLoginId(String loginId) {
        return userRepository.findById(loginId).isPresent();
    }
}
