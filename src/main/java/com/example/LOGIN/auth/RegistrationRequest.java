package com.example.LOGIN.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "Firstname is mandatory")
    @NotBlank(message = "Firstname is mandatory")
    private String firstName;
    @NotEmpty(message = "lastName is mandatory")
    @NotBlank(message = "lastName is mandatory")
    private String lastName;
    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String email;
    @Email(message = "password is not formatted")
    @NotEmpty(message = "password is mandatory")
    @Size(min = 8,message = "Password should be 8 characters  minimum")
    private String password;
}
