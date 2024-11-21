package com.hadiyasa.springRestfulAPI.service;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.RegisterUserRequest;
import com.hadiyasa.springRestfulAPI.model.response.UserResponse;
import org.springframework.stereotype.Service;

public interface UserService {
    void registerUser(RegisterUserRequest userRequest) throws Exception;
    UserResponse getUserDetails(User user);
}
