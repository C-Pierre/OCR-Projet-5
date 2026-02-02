package com.openclassrooms.mddapi.comment.controller;

import java.util.List;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.openclassrooms.mddapi.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.comment.service.CreateCommentService;
import com.openclassrooms.mddapi.comment.service.GetCommentsByPostService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class CommentControllerTest {

    private MockMvc mockMvc;
    private CreateCommentService createCommentService;
    private GetCommentsByPostService getCommentsByPostService;
    private ObjectMapper objectMapper;
    private CommentController commentController;

    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        createCommentService = Mockito.mock(CreateCommentService.class);
        getCommentsByPostService = Mockito.mock(GetCommentsByPostService.class);
        objectMapper = new ObjectMapper();

        commentController = new CommentController(createCommentService, getCommentsByPostService);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        commentDto = new CommentDto(1L, "My comment", 10L, 5L, "author", null, null);
    }

    @Test
    void getByPost_shouldReturnListOfComments() throws Exception {
        Mockito.when(getCommentsByPostService.execute(10L))
                .thenReturn(List.of(commentDto));

        mockMvc.perform(get("/api/comments/post/{postId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("My comment"))
                .andExpect(jsonPath("$[0].postId").value(10))
                .andExpect(jsonPath("$[0].authorId").value(5))
                .andExpect(jsonPath("$[0].authorUsername").value("author"));
    }

    @Test
    void create_shouldReturnCreatedComment() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest("New comment", 10L, 5L);
        Mockito.when(createCommentService.execute(any(CreateCommentRequest.class)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My comment"))
                .andExpect(jsonPath("$.postId").value(10))
                .andExpect(jsonPath("$.authorId").value(5))
                .andExpect(jsonPath("$.authorUsername").value("author"));
    }
}
