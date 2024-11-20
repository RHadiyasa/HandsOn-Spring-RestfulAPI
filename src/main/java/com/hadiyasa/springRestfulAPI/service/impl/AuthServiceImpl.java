package com.hadiyasa.springRestfulAPI.service.impl;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.LoginUserRequest;
import com.hadiyasa.springRestfulAPI.model.response.TokenResponse;
import com.hadiyasa.springRestfulAPI.repository.UserRepository;
import com.hadiyasa.springRestfulAPI.service.AuthService;
import com.hadiyasa.springRestfulAPI.service.ValidationService;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final ConfigurationPropertiesAutoConfiguration configurationPropertiesAutoConfiguration;

    public AuthServiceImpl(UserRepository userRepository, ValidationService validationService, ConfigurationPropertiesAutoConfiguration configurationPropertiesAutoConfiguration) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.configurationPropertiesAutoConfiguration = configurationPropertiesAutoConfiguration;
    }

    @Override
    @Transactional
    public TokenResponse login(LoginUserRequest loginUserRequest) {
        validationService.validate(loginUserRequest);

        User user = userRepository.findById(loginUserRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        // Bcrypt validation
        if (!BCrypt.checkpw(loginUserRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(System.currentTimeMillis() + next30Days());
        userRepository.save(user);

        return TokenResponse.builder().token(user.getToken()).expiredAt(user.getTokenExpiredAt()).build();
    }

    private Long next30Days(){
        return System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000);
    }

}
