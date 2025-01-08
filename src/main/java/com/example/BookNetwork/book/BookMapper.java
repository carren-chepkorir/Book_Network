package com.example.BookNetwork.book;

import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    Book toBook(BookRequest request){
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .sharable(request.sharable())
                .archived(false)

                .build();
    }


}
