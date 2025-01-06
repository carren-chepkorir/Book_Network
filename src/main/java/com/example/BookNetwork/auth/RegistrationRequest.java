package com.example.BookNetwork.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {
    @JsonProperty(value = "firstname")
    @NotBlank(message = "Firstname is mandatory")
    private String firstName;
    @NotBlank(message = "lastName is mandatory")
    @JsonProperty(value = "lastname")
    private String lastName;
    @JsonProperty(value = "email")
    @Email(message = "Email is not formatted")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "password is mandatory")
    @Size(min = 8,message = "Password should be 8 characters  minimum")
//    @Pattern(
//            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//            message = "Password must contain at least one letter, one number, and one special character"
//    )
    private String password;
}
