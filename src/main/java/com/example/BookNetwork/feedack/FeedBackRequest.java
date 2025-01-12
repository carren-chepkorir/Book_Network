package com.example.BookNetwork.feedack;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record FeedBackRequest(

        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 0, message = "202")
        Double note,
        @NotBlank(message = "203")
        @NotEmpty(message = "203")
        @NotNull(message = "203")
        String comment,
        @NotNull(message = "204")
        BigDecimal bookId
) {
}
