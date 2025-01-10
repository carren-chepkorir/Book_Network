package com.example.BookNetwork.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, BigDecimal> {

    @Query("SELECT history" +
            " FROM BookTransactionHistory bh" +
            " WHERE bh.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, BigDecimal userId);
}
