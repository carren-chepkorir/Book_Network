package com.example.BookNetwork.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface BookRepository  extends JpaRepository<Book,BigDecimal> {
@Query(
        "SELECT book\n" +
                "            FROM Book book\n" +
                "            WHERE book.archived = false\n" +
                "            AND book.sharable = true\n" +
                "            AND book.owner.id ! =:userId"
)
    Page<Book> findAllDisplayableBooks(Pageable pageable, BigDecimal userId);
}
