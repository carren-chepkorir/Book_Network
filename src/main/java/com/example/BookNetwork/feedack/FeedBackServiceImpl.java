package com.example.BookNetwork.feedack;

import com.example.BookNetwork.book.Book;
import com.example.BookNetwork.book.BookRepository;
import com.example.BookNetwork.book.exception.OperationNotPermittedException;
import com.example.BookNetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
@RequiredArgsConstructor
@Service

public class FeedBackServiceImpl implements FeedBackService{
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedBackRepository feedBackRepository;
    @Override
    public BigDecimal saveFeedback(FeedBackRequest request, Authentication connectedUser) {
        Book book=bookRepository.findById(request.bookId())
                .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ request.bookId()));

        if (book.isArchived() || !book.isSharable()){
            throw new OperationNotPermittedException("You cannot give Feedback for this book since it is archived or Not Sharable");
        }
        User user=(User) connectedUser.getPrincipal();

        if (Objects.equals(book.getOwner().getId(),user.getId())){
            throw new  OperationNotPermittedException("You cannot give Feedback for your own book");
        }
        Feedback feedback=feedbackMapper.toFeedback(request);
        return feedBackRepository.save(feedback).getId();
    }
}
