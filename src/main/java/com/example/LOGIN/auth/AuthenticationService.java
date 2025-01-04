package com.example.LOGIN.auth;

import com.example.LOGIN.role.RoleRepository;
import com.example.LOGIN.user.Token;
import com.example.LOGIN.user.TokenRepository;
import com.example.LOGIN.user.User;
import com.example.LOGIN.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    public void register(RegistrationRequest request) {
        //default role of user is just user
        var userRole=roleRepository.findByName("USER")
                .orElseThrow(()->new IllegalStateException("ROLE USER was not initialized"));
        var user= User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) {
        var newToken=generateAndSaveActivationToken(user);

    }

    private Object generateAndSaveActivationToken(User user) {
        String generatedToken=generateAndSaveActivationCode(6);
        var token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateAndSaveActivationCode(int length) {
        String characters="0123456789";
        StringBuilder codeBuilder=new StringBuilder();
        SecureRandom secureRandom=new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex=secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));

        }
        return codeBuilder.toString();
    }
}
