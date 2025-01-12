package com.example.BookNetwork.feedack;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface FeedBackRepository  extends JpaRepository<Feedback, BigDecimal> {
}
