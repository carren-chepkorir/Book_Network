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
public class BorrowedBooksResponse {
    private BigDecimal id;
    private  String title;
    private String authorName;
    private  String isbn;
    private  Double rate;
    private boolean returned;
    private boolean returnedApproved;
}
