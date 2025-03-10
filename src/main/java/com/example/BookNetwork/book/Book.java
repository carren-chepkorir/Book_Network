package com.example.BookNetwork.book;

import com.example.BookNetwork.common.BaseEntity;
import com.example.BookNetwork.feedack.Feedback;
import com.example.BookNetwork.history.BookTransactionHistory;
import com.example.BookNetwork.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
public class Book extends BaseEntity {
    private String title;
    private  String synopsis;
    private String isbn;
    private String bookCover;
    private String authorName;
    private boolean archived;
    private boolean sharable;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;


    @Transient
    public  double getRate(){
        if (feedbacks==null || feedbacks.isEmpty()){
            return 0.0;
        }
        Double rate=this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        double roundedRate=Math.round(rate *10.0)/10.0;
        return roundedRate;
    }
}
