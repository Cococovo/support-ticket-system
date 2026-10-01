package com.nilay.support.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import com.nilay.support.dto.request.CommentRequest;
import com.nilay.support.dto.response.CommentResponse;
import com.nilay.support.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{ticketId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long ticketId,
            Authentication authentication,
            @Valid @RequestBody CommentRequest request){

        String email = authentication.getName();
        return ResponseEntity.ok(commentService.addComment(ticketId, email, request));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long ticketId){
        return ResponseEntity.ok(commentService.getComments(ticketId));
    }
}