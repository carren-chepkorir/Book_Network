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

public class BookResponse {
    private BigDecimal id;
    private  String title;
    private String authorName;
    private  String isbn;
    private  String synopsis;
    private  String owner;
    private  byte[] cover;
    private  Double rate;
    private boolean archived;
    private boolean sharable;
}
