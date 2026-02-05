package com.openclassrooms.mddapi.api.post.controller;

import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import org.springframework.security.access.prepost.PreAuthorize;
import com.openclassrooms.mddapi.api.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.application.post.service.GetPostService;
import com.openclassrooms.mddapi.application.post.service.CreatePostService;
import com.openclassrooms.mddapi.application.post.service.GetPostsForUserFeed;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final GetPostsForUserFeed getPostWithUserSubscriptionsService;
    private final CreatePostService createPostService;
    private final GetPostService getPostService;

    public PostController(
        GetPostsForUserFeed getPostWithUserSubscriptionsService,
        CreatePostService createPostService,
        GetPostService getPostService
    ) {
        this.getPostWithUserSubscriptionsService = getPostWithUserSubscriptionsService;
        this.createPostService = createPostService;
        this.getPostService = getPostService;
    }

    @Operation(summary = "Get a post by ID", description = "Returns one post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getById(@PathVariable Long postId) {
        PostDto posts = getPostService.execute(postId);
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "Get a post by ID with user subscription info",
        description = "Returns one post by ID with user subscription"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Posts not found")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("@subscriptionAuthorization.canGet(#userId)")
    public ResponseEntity<List<PostDto>> getByUserSubscriptions(@PathVariable Long userId) {
        List<PostDto> posts = getPostWithUserSubscriptionsService.execute(userId);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Create a new post", description = "Creates a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid post data")
    })
    @PostMapping
    public ResponseEntity<PostDto> create(@Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.status(201).body(createPostService.execute(request));
    }
}