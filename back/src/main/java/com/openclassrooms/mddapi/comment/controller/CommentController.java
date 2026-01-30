package com.openclassrooms.mddapi.comment.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import com.openclassrooms.mddapi.comment.service.CreateCommentService;
import com.openclassrooms.mddapi.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.comment.service.GetCommentsByPostService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CreateCommentService createCommentService;
    private final GetCommentsByPostService getCommentsByPostService;

    public CommentController(
        CreateCommentService createCommentService,
        GetCommentsByPostService getCommentsByPostService
    ) {
        this.createCommentService = createCommentService;
        this.getCommentsByPostService = getCommentsByPostService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getByPost(@PathVariable Long postId) {
        List<CommentDto> comments = getCommentsByPostService.execute(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(201).body(createCommentService.execute(request));
    }
}