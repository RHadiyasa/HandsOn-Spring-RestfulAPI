package com.hadiyasa.springRestfulAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.LoginUserRequest;
import com.hadiyasa.springRestfulAPI.model.response.TokenResponse;
import com.hadiyasa.springRestfulAPI.model.response.WebResponse;
import com.hadiyasa.springRestfulAPI.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Type;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Delete data sebelum test
    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    void loginFailed() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("admin");
        loginUserRequest.setPassword("admin");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<WebResponse<String>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    });
            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void invalidUsernameOrPassword() throws Exception {
        User user = new User();
        user.setUsername("admin1");
        user.setPassword(BCrypt.hashpw("admin1", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("admin1");
        loginUserRequest.setPassword("wrongPassword1");


        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<WebResponse<String>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    });
            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginSuccess() throws Exception {
        User loggedInUser = new User();
        loggedInUser.setUsername("admin2");
        loggedInUser.setPassword(BCrypt.hashpw("admin2", BCrypt.gensalt()));
        userRepository.save(loggedInUser);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("admin2");
        loginUserRequest.setPassword("admin2");


        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<WebResponse<TokenResponse>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    });

            Assertions.assertNull(response.getErrors());
            Assertions.assertNotNull(response.getData().getToken());
            Assertions.assertNotNull(response.getData().getExpiredAt());

            User userDb = userRepository.findById("admin2").orElse(null);
            Assertions.assertNotNull(userDb);

            System.out.println("Database Token Expired At: " + userDb.getTokenExpiredAt());
            System.out.println("Response Token Expired At: " + response.getData().getExpiredAt());
            Assertions.assertEquals(userDb.getToken(), response.getData().getToken());
            Assertions.assertEquals(userDb.getTokenExpiredAt(), response.getData().getExpiredAt());
        });
    }
}
