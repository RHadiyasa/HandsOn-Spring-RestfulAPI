package com.hadiyasa.springRestfulAPI.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateContactRequest {

    /** JsonIgnore : Menandakan bahwa id di dalam json tidak perlu dibaca */
    @JsonIgnore
    @NotBlank
    private String id;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    private String phoneNumber;
}
