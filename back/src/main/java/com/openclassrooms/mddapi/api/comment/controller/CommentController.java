package com.openclassrooms.mddapi.api.comment.controller;

import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.openclassrooms.mddapi.application.comment.dto.CommentDto;
import com.openclassrooms.mddapi.api.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.application.comment.service.CreateCommentService;
import com.openclassrooms.mddapi.application.comment.service.GetCommentsByPostService;

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

    @Operation(summary = "Get a comment by POST_ID", description = "Returns all comments by POST_ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Comments not found")
    })
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getByPost(@PathVariable Long postId) {
        List<CommentDto> comments = getCommentsByPostService.execute(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Create a new comment", description = "Creates a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment data")
    })
    @PostMapping()
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(201).body(createCommentService.execute(request));
    }
}