package com.example.BookNetwork.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDto {
    private String title;
    private String synopsis;
    private String isbn;
    private String bookCover;
    private String authorName;
    private Boolean archived;
    private Boolean sharable;
    // To link a book to an owner (User), include the owner's id.
    private BigDecimal ownerId;
}
