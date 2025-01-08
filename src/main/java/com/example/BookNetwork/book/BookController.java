package com.example.BookNetwork.book;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "BookController")
public class BookController {

    private final BookService bookService;
    public ResponseEntity<BigDecimal> saveBook
            (@RequestBody @Valid BookRequest request,
             Authentication connectedUser)
    {
        return ResponseEntity.ok(bookService.save(request,connectedUser));
    }
}
