package com.example.BookNetwork.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
            //@Valid annotation validates all fields marked as required in request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) throws MessagingException {
         AuthenticationResponse response=authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate-account")
    public  void activateAccount(
            @RequestParam String token
    ) throws MessagingException {
  authenticationService.activateAccount(token);
    }
}
