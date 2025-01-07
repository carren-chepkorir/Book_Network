package com.example.BookNetwork.auth;

import com.example.BookNetwork.email.EmailService;
import com.example.BookNetwork.email.EmailTemplateName;
import com.example.BookNetwork.role.RoleRepository;
import com.example.BookNetwork.security.JwtService;
import com.example.BookNetwork.user.Token;
import com.example.BookNetwork.user.TokenRepository;
import com.example.BookNetwork.user.User;
import com.example.BookNetwork.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
   @Value("${application.mailing.frontend.activation-url}")
    public  String activationUrl;


   public void register(RegistrationRequest request) throws MessagingException {
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

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken=generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                (String) newToken,
                "Account activation"

        );

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
        //Secure randomness ensures the tokens are hard to predict
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
        var auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims=new HashMap<String ,Object>();
        var user=((User)auth.getPrincipal());
        claims.put("fullName",user.fullName());
        var jwtToken=jwtService.generateToken(claims,user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();

    }

    public void activateAccount(String token) throws MessagingException {
       Token savedToken=tokenRepository.findByToken(token)
               .orElseThrow(()->new RuntimeException("Invalid Token"));
       if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())){
           sendValidationEmail(savedToken.getUser());
           throw  new RuntimeException("Activation token has expired.A new token has been sent to the same email address");
       }
        var user=userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
       user.setEnabled(true);
       userRepository.save(user);
       savedToken.setValidatedAt(LocalDateTime.now());
       tokenRepository.save(savedToken);

    }
}