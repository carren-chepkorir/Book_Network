package com.example.BookNetwork.history;

import com.example.BookNetwork.book.Book;
import com.example.BookNetwork.common.BaseEntity;
import com.example.BookNetwork.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
public class BookTransactionHistory extends BaseEntity {
    private Boolean returned;
    private Boolean returnedApproved;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
