package com.example.BookNetwork.feedack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedBackResponse {
    private Double note;
    private String comment;
    private boolean ownFeedback;
}
