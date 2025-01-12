package com.example.BookNetwork.feedack;

import com.example.BookNetwork.book.Book;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedBackRequest request) {

        return  Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .build())
                .build();
    }

    public FeedBackResponse toFeedbackResponse(Feedback feedback, BigDecimal id) {
            return FeedBackResponse.builder()
                    .note(feedback.getNote())
                    .comment(feedback.getComment())
                    .ownFeedback(Objects.equals(feedback.getCreatedBy(),id))
                    .build();
    }
}
