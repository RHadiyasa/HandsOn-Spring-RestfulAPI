package com.hadiyasa.springRestfulAPI.service.impl;

import com.hadiyasa.springRestfulAPI.service.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;

    public ValidationServiceImpl(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object request) {
        /** Validator memeriksa apakah userRequest memenuhi aturan validasi yang didefinisikan sebelumnya?
         * Jika ada violation, maka mengembalikan sebuah SET yang berisi error dari userRequest */

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
