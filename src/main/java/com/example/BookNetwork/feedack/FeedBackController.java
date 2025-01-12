package com.example.BookNetwork.feedack;

import com.example.BookNetwork.book.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
public class FeedBackController {
    private final FeedBackService feedBackService;

    public ResponseEntity<BigDecimal>saveFeedBack(
           @Valid @RequestBody FeedBackRequest request,
           Authentication connectedUser
    ){
    return ResponseEntity.ok(feedBackService.saveFeedback(request,connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> getFeedBacks(
        @PathVariable("book-id") BigDecimal bookId,
        @RequestParam(value = "pageNo",defaultValue = "0")Integer pageNo,
        @RequestParam(value = "pageSize",defaultValue = "100")Integer pageSize,
        Authentication connectedUser
        ){
         return ResponseEntity.ok(feedBackService.getFeedbacks(bookId,pageNo,pageSize,connectedUser));
    }
}
