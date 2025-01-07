package com.example.BookNetwork.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email(message = "Email is not formatted")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "password is mandatory")
    @Size(min = 8,message = "Password should be 8 characters  minimum")
    private String password;
}
