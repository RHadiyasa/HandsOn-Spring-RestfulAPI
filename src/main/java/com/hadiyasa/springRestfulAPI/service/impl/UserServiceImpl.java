package com.hadiyasa.springRestfulAPI.service.impl;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.RegisterUserRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateUserRequest;
import com.hadiyasa.springRestfulAPI.model.response.UserResponse;
import com.hadiyasa.springRestfulAPI.repository.UserRepository;
import com.hadiyasa.springRestfulAPI.service.UserService;
import com.hadiyasa.springRestfulAPI.service.ValidationService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ValidationService validationService;

    public UserServiceImpl(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Override
    @Transactional
    public void registerUser(RegisterUserRequest userRequest) {
        validationService.validate(userRequest);

        if (userRepository.existsById(userRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt())); // password akan di hashing
        user.setName(userRequest.getName());

        userRepository.save(user);
    }

    @Override
    public UserResponse getUserDetails(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    @Override
    public UserResponse updateUser(User user, UpdateUserRequest userRequest) {
        validationService.validate(userRequest);

        if (Objects.nonNull(userRequest.getName())){
            user.setName(userRequest.getName());
        }

        if (Objects.nonNull(userRequest.getPassword())){
            user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);
        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();
    }
}
