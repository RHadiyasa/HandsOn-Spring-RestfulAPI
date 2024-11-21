package com.hadiyasa.springRestfulAPI.controller;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.RegisterUserRequest;
import com.hadiyasa.springRestfulAPI.model.response.UserResponse;
import com.hadiyasa.springRestfulAPI.model.response.WebResponse;
import com.hadiyasa.springRestfulAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<String> registerUser(@RequestBody RegisterUserRequest registerUserRequest) throws Exception {
        userService.registerUser(registerUserRequest);
        return WebResponse.<String>builder().data("User registered successfully").build(); // dari annotation builder
    }

    @GetMapping(path = "/api/users/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> getUser(User user) {
        UserResponse userResponse = userService.getUserDetails(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
}
