package com.example.BookNetwork.feedack;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

public interface FeedBackService {
    BigDecimal saveFeedback(@Valid FeedBackRequest request, Authentication connectedUser);
}
