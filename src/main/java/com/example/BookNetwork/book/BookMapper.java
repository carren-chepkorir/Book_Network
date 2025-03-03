package com.example.BookNetwork.book;

import com.example.BookNetwork.file.FileUtils;
import com.example.BookNetwork.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookMapper {
    Book toBook(BookRequest request){
        return Book.builder()
//                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .sharable(request.sharable())
                .archived(false)
                .build();

    }
BookResponse toBookResponse(Book book){
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .owner(book.getOwner().fullName())
                .archived(book.isArchived())
                .sharable(book.isSharable())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))

                .build();

}

    public BorrowedBooksResponse toBorrowedBooks(BookTransactionHistory bookTransactionHistory) {
        return BorrowedBooksResponse.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .authorName(bookTransactionHistory.getBook().getAuthorName())
                .rate(bookTransactionHistory.getBook().getRate())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .returned(bookTransactionHistory.isReturned())
                .returnedApproved(bookTransactionHistory.isReturnedApproved())
                .build();
    }
}
