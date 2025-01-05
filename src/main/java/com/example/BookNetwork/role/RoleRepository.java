package com.example.BookNetwork.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, BigDecimal> {

    Optional<Roles> findByName(String name );
}
