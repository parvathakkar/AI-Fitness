package com.fitness.gateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email Required")
    @Email(message = "invalid Email")
    private String email;

    private String keyCloakId;


    @NotBlank(message = "Password Required")
    @Size(message = "Password must be at least 6 characters")
    private String password;

    private String firstName;
    private String lastName;
}
