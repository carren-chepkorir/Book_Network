package com.example.LOGIN.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, BigDecimal> {

    Optional<Roles> findByName(String name );
}
