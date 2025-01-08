package com.example.BookNetwork.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface BookRepository  extends JpaRepository<Book,BigDecimal> {
}
