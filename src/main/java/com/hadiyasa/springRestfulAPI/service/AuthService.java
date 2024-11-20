package com.hadiyasa.springRestfulAPI.service;


import com.hadiyasa.springRestfulAPI.model.request.LoginUserRequest;
import com.hadiyasa.springRestfulAPI.model.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginUserRequest loginUserRequest);
}
