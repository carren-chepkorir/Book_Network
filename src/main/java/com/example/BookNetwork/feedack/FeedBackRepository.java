package com.example.BookNetwork.feedack;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface FeedBackRepository  extends JpaRepository<Feedback, BigDecimal> {

    @Query("SELECT feedBack " +
            "FROM Feedback feedback" +
            " WHERE feedback.book.id = :bookId")
    Page<Feedback> findAllByBookId(BigDecimal bookId, Pageable pageable);
}
