package com.openclassrooms.mddapi.comment.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import com.openclassrooms.mddapi.comment.service.GetCommentsByPostService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final GetCommentsByPostService getCommentsByPostService;

    public CommentController(
        GetCommentsByPostService getCommentsByPostService
    ) {
        this.getCommentsByPostService = getCommentsByPostService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getByPost(@PathVariable Long postId) {
        List<CommentDto> comments = getCommentsByPostService.execute(postId);
        return ResponseEntity.ok(comments);
    }
}