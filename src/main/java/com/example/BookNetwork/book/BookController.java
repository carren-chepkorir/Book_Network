package com.example.BookNetwork.book;

import com.example.BookNetwork.common.GenericResponse;
import com.example.BookNetwork.common.ResponseStatusEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("books")
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
   @GetMapping("{id}")
    public ResponseEntity<GenericResponse<BookResponse>> findBookById(

            @PathVariable(name = "id") BigDecimal id
    ){
        GenericResponse<BookResponse> response=bookService.findBookById(id);
        if (response !=null && response.getStatus()== ResponseStatusEnum.SUCCESS){
      return  ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(response);

    }
    @GetMapping()
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "pageNo",defaultValue = "0",required = false)Integer pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false)Integer pageSize,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBooks(pageNo,pageSize,connectedUser));

    }
    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "pageNo",defaultValue = "0",required = false)Integer pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false)Integer pageSize,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBooksByOwner(pageNo,pageSize,connectedUser));

    }
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllBorrowedBooks(
            @RequestParam(name = "pageNo",defaultValue = "0",required = false)Integer pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false)Integer pageSize,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(pageNo,pageSize,connectedUser));

    }
}
