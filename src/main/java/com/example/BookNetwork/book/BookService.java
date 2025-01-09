package com.example.BookNetwork.book;

import com.example.BookNetwork.common.GenericResponse;
import com.example.BookNetwork.common.ResponseStatusEnum;
import com.example.BookNetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    public BigDecimal save(@Valid BookRequest request, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Book book=bookMapper.toBook(request);
        book.setOwner(user);
        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }


    public GenericResponse<BookResponse> findBookById(BigDecimal id) {
            Book entity=bookRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Book not found for the given ID:: "+id));
            BookResponse bookResponse=bookMapper.toBookResponse(entity);
            return GenericResponse.<BookResponse>builder()
                    .message("Successfully fetched the book with the given ID:: "+id)
                    .status(ResponseStatusEnum.ERROR)
                    ._embedded(bookResponse)
                    .build();

    }

    public PageResponse<BookResponse> findAllBooks(Integer pageNo, Integer pageSize, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by("createdDate").descending());
        Page<Book> books=bookRepository.findAllDisplayableBooks(pageable,user.getId());

        List<BookResponse> bookResponses=books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return  new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }
}
