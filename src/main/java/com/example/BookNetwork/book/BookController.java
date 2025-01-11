package com.example.BookNetwork.book;

import com.example.BookNetwork.common.GenericResponse;
import com.example.BookNetwork.common.ResponseStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllReturnedBooks(
            @RequestParam(name = "pageNo",defaultValue = "0",required = false)Integer pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false)Integer pageSize,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(pageNo,pageSize,connectedUser));

    }

    @PatchMapping("/sharable/{book-id}")
    public ResponseEntity<BigDecimal> updateSharableStatus(
            @PathVariable("{book-id}") BigDecimal bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateSharableStatus(bookId,connectedUser));
    }
    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<BigDecimal> updateArchivedStatus(
            @PathVariable("{book-id}") BigDecimal bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId,connectedUser));
    }
    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<BigDecimal> borrowBook(
            @PathVariable("{book-id}") BigDecimal bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.borrowBook(bookId,connectedUser));
    }
    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<BigDecimal> returnBorrowedBook(
            @PathVariable("{book-id}") BigDecimal bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId,connectedUser));
    }
    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<BigDecimal> ApproveReturnedBorrowedBook(
            @PathVariable("{book-id}") BigDecimal bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.ApproveReturnedBorrowedBook(bookId,connectedUser));
    }
    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") BigDecimal bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ) {
        bookService.uploadBookCoverPicture(file, connectedUser, bookId);
        return ResponseEntity.accepted().build();
    }
}
