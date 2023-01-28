package com.example.myreform.User.service;

import com.example.myreform.User.dto.UserLoginDto;
import com.example.myreform.User.dto.UserSignupDto;
import com.example.myreform.User.response.ResponseProfile;
import com.example.myreform.User.response.ResponseUser;

public interface UserService {

    ResponseUser signUp(UserSignupDto signupDTO);
    ResponseUser login(UserLoginDto loginDTO);
    Object find(Long userId);
}
