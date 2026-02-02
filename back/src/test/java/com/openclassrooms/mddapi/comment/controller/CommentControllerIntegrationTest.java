package com.openclassrooms.mddapi.comment.controller;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.openclassrooms.mddapi.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.comment.service.CreateCommentService;
import com.openclassrooms.mddapi.comment.service.GetCommentsByPostService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CommentControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCommentService createCommentService;

    @MockBean
    private GetCommentsByPostService getCommentsByPostService;

    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        CommentController commentController = new CommentController(createCommentService, getCommentsByPostService);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        commentDto = new CommentDto(
            1L,
            "My comment",
            10L,
            5L,
            "author",
            null,
            null
        );
    }

    @Test
    void getByPost_shouldReturnListOfComments() throws Exception {
        when(getCommentsByPostService.execute(10L)).thenReturn(List.of(commentDto));

        mockMvc.perform(get("/api/comments/post/{postId}", 10L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].content").value("My comment"));
    }

    @Test
    void create_shouldReturnCreatedComment() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest("New comment", 10L, 5L);
        when(createCommentService.execute(any(CreateCommentRequest.class))).thenReturn(commentDto);

        mockMvc.perform(post("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.content").value("My comment"));
    }

    @Test
    void create_shouldFailValidation_whenContentBlank() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest("", 10L, 5L);

        mockMvc.perform(post("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
