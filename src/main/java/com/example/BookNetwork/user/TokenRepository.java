package com.example.BookNetwork.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, BigDecimal> {

    Optional<Token> findByToken(String token);
}
