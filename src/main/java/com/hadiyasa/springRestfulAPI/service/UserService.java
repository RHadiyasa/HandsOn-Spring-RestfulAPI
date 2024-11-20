package com.hadiyasa.springRestfulAPI.service;

import com.hadiyasa.springRestfulAPI.model.request.RegisterUserRequest;
import org.springframework.stereotype.Service;

public interface UserService {
    void registerUser(RegisterUserRequest userRequest) throws Exception;
}
