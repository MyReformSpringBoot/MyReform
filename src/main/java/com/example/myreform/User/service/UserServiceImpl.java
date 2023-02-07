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
    @Autowired
    private final BoardRepository boardRepository;


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
                String token = user.get().getNickname();
                return new ResponseUser(ExceptionCode.LOGIN_OK, token);
            }
            return new ResponseUser(ExceptionCode.LOGIN_NOT_FOUND_PW);
        }
        // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
        return new ResponseUser(ExceptionCode.LOGIN_NOT_FOUND_ID);
    }


    @Override
    public Object find(String nickname) {
        Optional<User> userOp = userRepository.findByNickname(nickname);
        if(userOp.isEmpty()){
            return new ResponseUser(ExceptionCode.USER_NOT_FOUND);
        }
        User user= userOp.get();
        UserFindDto findDto = user.toFindDto(userOp);
        return new ResponseProfile(ExceptionCode.USER_GET_OK, findDto);//, getLikeBoards(findDto.getLikeBoardsId()

    }
    private List<Board> getLikeBoards(List<Long> boardIds){
        List<Board> boards = new ArrayList<>();
        for(Long id : boardIds){
            boards.add(boardRepository.findById(id).get());
        }
        return boards;
    }

    @Override
    public Object update(String nickname, UserUpdateDto userUpdateDto) {

        Optional<User> userOp = userRepository.findByNickname(nickname);
        if(userOp.isEmpty()){
            return new ResponseUser(ExceptionCode.USER_NOT_FOUND);
        }

        if (userOp.isPresent()){//기존: userRepository.findByNickname(userUpdateDto.getNickname()).isPresent()
            return new ResponseUser(ExceptionCode.SIGNUP_DUPLICATED_NICKNAME);
        }

        User user = userOp.get();
        try {
            String encodePw = passwordEncoder.encode(userUpdateDto.getPw());
            user.update(userUpdateDto, encodePw);
        } catch (RuntimeException exception) {
            return new ResponseUser(ExceptionCode.USER_UPDATE_INVALID);
        }
        return new ResponseProfile(ExceptionCode.USER_UPDATE_OK, user.toFindDto(userOp));
    }

}
