package com.example.myreform.User.service;

import com.example.myreform.User.dto.UserLoginDto;
import com.example.myreform.User.dto.UserSignupDto;
import com.example.myreform.User.dto.UserUpdateDto;
import com.example.myreform.User.response.ResponseProfile;
import com.example.myreform.User.response.ResponseUser;

public interface UserService {

    Object signUp(UserSignupDto signupDTO);
    Object login(UserLoginDto loginDTO);
    Object find(String loginId);
    Object update(String loginId, UserUpdateDto userUpdateDto);
}
