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
}
