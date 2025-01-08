package com.example.BookNetwork.book;


import java.math.BigDecimal;

public record BookRequest(
        BigDecimal id,
        String title,
        String authorName,
        String isbn,
        String synopsis,
        Boolean  sharable
) {


}
