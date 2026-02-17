package com.openclassrooms.mddapi.api.post.controller;

import java.util.List;

import com.openclassrooms.mddapi.api.post.controller.PostController;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.mddapi.application.post.service.GetPostService;
import com.openclassrooms.mddapi.api.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.application.post.service.CreatePostService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.openclassrooms.mddapi.application.post.service.GetPostsForUserFeed;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class PostControllerTest {

    private MockMvc mockMvc;
    private GetPostService getPostService;
    private CreatePostService createPostService;
    private GetPostsForUserFeed getPostsForUserFeed;
    private ObjectMapper objectMapper;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        getPostService = Mockito.mock(GetPostService.class);
        createPostService = Mockito.mock(CreatePostService.class);
        getPostsForUserFeed = Mockito.mock(GetPostsForUserFeed.class);
        objectMapper = new ObjectMapper();

        PostController postController = new PostController(getPostsForUserFeed, createPostService, getPostService);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

        postDto = new PostDto(
            1L,
            "Post Title",
            "Post Content",
            1L,
            "Math",
            1L,
            "author",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void getById_shouldReturnPost() throws Exception {
        Mockito.when(getPostService.execute(1L)).thenReturn(postDto);

        mockMvc.perform(get("/api/posts/{postId}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Post Title"))
            .andExpect(jsonPath("$.content").value("Post Content"))
            .andExpect(jsonPath("$.subjectName").value("Math"))
            .andExpect(jsonPath("$.authorUsername").value("author"));
    }

    @Test
    void getByUserSubscriptions_shouldReturnListOfPosts() throws Exception {
        List<PostDto> posts = List.of(postDto);
        Mockito.when(getPostsForUserFeed.execute(1L)).thenReturn(posts);

        mockMvc.perform(get("/api/posts/user/{userId}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Post Title"))
            .andExpect(jsonPath("$[0].content").value("Post Content"));
    }

    @Test
    void create_shouldReturnCreatedPost() throws Exception {
        CreatePostRequest request = new CreatePostRequest(
            "New Post",
            "New Post",
            1L,
            1L
        );
        PostDto createdDto = new PostDto(
            2L,
            "New Post",
            "Content of new post",
            1L,
            "Math",
            1L,
            "author",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Mockito.when(createPostService.execute(any(CreatePostRequest.class))).thenReturn(createdDto);

        mockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.title").value("New Post"))
            .andExpect(jsonPath("$.content").value("Content of new post"))
            .andExpect(jsonPath("$.subjectName").value("Math"))
            .andExpect(jsonPath("$.authorUsername").value("author"));
    }
}
