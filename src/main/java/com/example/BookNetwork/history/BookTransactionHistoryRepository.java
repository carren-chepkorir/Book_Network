package com.example.BookNetwork.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, BigDecimal> {

    @Query("SELECT history " +
            " FROM BookTransactionHistory history " +
            " WHERE history.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, BigDecimal userId);
    @Query("SELECT history " +
            " FROM BookTransactionHistory history " +
            " WHERE history.book.owner.id = :userId")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, BigDecimal userId);
    @Query("SELECT (COUNT(*) > 0) " +
            "FROM BookTransactionHistory history " +
            "WHERE history.user.id = :userId " +
            "AND history.book.id = :bookId " +
            "AND history.returnedApproved = false")
    boolean isAlreadyBorrowed(BigDecimal bookId, BigDecimal userId);
    @Query("SELECT transaction " +
            "FROM BookTransactionHistory transaction " +
            "WHERE transaction.user.id = :userId " +
            "AND transaction.book.id = :bookId " +
            "AND transaction.returned = false " +
            "AND transaction.returnedApproved = false")
    Optional<BookTransactionHistory> findBookByBookIdAndUserId(BigDecimal bookId, BigDecimal userId);
    @Query("SELECT transaction " +
            "FROM BookTransactionHistory transaction " +
            "WHERE transaction.book.createdBy = :userId " +
            "AND transaction.book.id = :bookId " +
            "AND transaction.returned = true ")
    Optional<BookTransactionHistory> findBookByBookIdAndOwnerId(BigDecimal bookId, BigDecimal userId);
}
