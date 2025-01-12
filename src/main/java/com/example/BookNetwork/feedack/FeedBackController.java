package com.example.BookNetwork.feedack;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
