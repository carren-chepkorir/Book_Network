package com.example.BookNetwork.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, BigDecimal> {

    @Query("SELECT history" +
            " FROM BookTransactionHistory bh" +
            " WHERE bh.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, BigDecimal userId);
    @Query("SELECT history" +
            " FROM BookTransactionHistory bh" +
            " WHERE bh.book.owner.id = :userId")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, BigDecimal id);
    @Query("SELECT" +
            "(COUNT(*)>0" +
            "FROM BookTransactionHistory history" +
            "WHERE history.user.id = :userId" +
            "AND history.book.id = :bookId" +
            "AND history.returnedApproved = false")

    boolean isAlreadyBorrowed(BigDecimal bookId, BigDecimal userId);
    @Query("SELECT transaction" +
            "FROM BookTransactionHistory history" +
            "WHERE history.user.id = :userId" +
            "AND history.book.id = :bookId" +
            "AND history.returned = false" +
            "AND history.returnedApproved = false")
    Optional<BookTransactionHistory> findBookByBookIdAndUserId(BigDecimal bookId, BigDecimal userId);
}
