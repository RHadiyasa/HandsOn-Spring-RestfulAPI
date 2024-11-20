package com.hadiyasa.springRestfulAPI.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {
    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 100)
    private String name;
}
