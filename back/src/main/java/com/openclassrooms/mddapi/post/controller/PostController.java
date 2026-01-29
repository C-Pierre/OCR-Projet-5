package com.openclassrooms.mddapi.post.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.service.GetPostsService;
import com.openclassrooms.mddapi.post.service.GetPostWithUserSubscriptionsService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final GetPostWithUserSubscriptionsService getPostWithUserSubscriptionsService;
    private final GetPostsService getPostsService;

    public PostController(
        GetPostWithUserSubscriptionsService getPostWithUserSubscriptionsService,
        GetPostsService getPostsService
    ) {
        this.getPostWithUserSubscriptionsService = getPostWithUserSubscriptionsService;
        this.getPostsService = getPostsService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll() {
        return ResponseEntity.ok(getPostsService.execute());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getByUserSubscriptions(@PathVariable Long userId) {
        List<PostDto> posts = getPostWithUserSubscriptionsService.execute(userId);
        return ResponseEntity.ok(posts);
    }
}