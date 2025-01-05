package com.example.BookNetwork.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, BigDecimal> {

    Optional<User> findByEmail(String email);
}
