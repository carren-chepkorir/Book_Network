package com.example.LOGIN.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
            //@Valid annotation validates all fields marked as required in request
    ){
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }
}
