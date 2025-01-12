package com.example.BookNetwork.feedack;

import com.example.BookNetwork.book.PageResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

public interface FeedBackService {
    BigDecimal saveFeedback(@Valid FeedBackRequest request, Authentication connectedUser);

    PageResponse<FeedBackResponse> getFeedbacks(BigDecimal bookId, Integer pageNo, Integer pageSize, Authentication connectedUser);
}
