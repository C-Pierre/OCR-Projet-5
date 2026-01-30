package com.openclassrooms.mddapi.post.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.service.GetPostService;
import com.openclassrooms.mddapi.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.post.service.CreatePostService;
import com.openclassrooms.mddapi.post.service.GetPostWithUserSubscriptionsService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final GetPostWithUserSubscriptionsService getPostWithUserSubscriptionsService;
    private final CreatePostService createPostService;
    private final GetPostService getPostService;

    public PostController(
        GetPostWithUserSubscriptionsService getPostWithUserSubscriptionsService,
        CreatePostService createPostService,
        GetPostService getPostService
    ) {
        this.getPostWithUserSubscriptionsService = getPostWithUserSubscriptionsService;
        this.createPostService = createPostService;
        this.getPostService = getPostService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getById(@PathVariable Long postId) {
        PostDto posts = getPostService.execute(postId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getByUserSubscriptions(@PathVariable Long userId) {
        List<PostDto> posts = getPostWithUserSubscriptionsService.execute(userId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.status(201).body(createPostService.execute(request));
    }
}