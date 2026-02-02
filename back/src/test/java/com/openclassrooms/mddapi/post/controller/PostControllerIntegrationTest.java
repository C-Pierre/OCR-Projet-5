package com.openclassrooms.mddapi.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.post.service.CreatePostService;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.subject.repository.SubjectRepository;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CreatePostService createPostService;

    private ObjectMapper objectMapper;
    private User author;
    private Subject subject;
    private User subscriber;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        long timestamp = System.currentTimeMillis();

        author = new User("author+" + timestamp + "@test.com", "author+" + timestamp);
        author.setPassword("password123");
        author = userRepository.save(author);

        subject = new Subject("Math+" + timestamp, "Math description");
        subject = subjectRepository.save(subject);

        subscriber = new User("subscriber+" + timestamp + "@test.com", "subscriber+" + timestamp);
        subscriber.setPassword("password123");
        subscriber = userRepository.save(subscriber);

        Subscription subscription = new Subscription(subscriber, subject);
        subscriptionRepository.save(subscription);
    }

    @Test
    @WithMockUser(username = "subscriber", roles = {"USER"})
    void createPost_and_getById_shouldWork() throws Exception {
        CreatePostRequest request = new CreatePostRequest(
                "Integration Test Post", "Post content", subject.getId(), author.getId()
        );

        String response = mockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Integration Test Post"))
            .andExpect(jsonPath("$.content").value("Post content"))
            .andExpect(jsonPath("$.authorId").value(author.getId()))
            .andExpect(jsonPath("$.subjectId").value(subject.getId()))
            .andReturn().getResponse().getContentAsString();

        PostDto postDto = objectMapper.readValue(response, PostDto.class);

        mockMvc.perform(get("/api/posts/{postId}", postDto.id())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Integration Test Post"))
            .andExpect(jsonPath("$.content").value("Post content"))
            .andExpect(jsonPath("$.authorId").value(author.getId()))
            .andExpect(jsonPath("$.subjectId").value(subject.getId()));
    }

    @Test
    void getByUserSubscriptions_shouldReturnPosts() throws Exception {
        CreatePostRequest request1 = new CreatePostRequest(
            "Post 1", "Content 1", subject.getId(), author.getId()
        );
        CreatePostRequest request2 = new CreatePostRequest(
            "Post 2", "Content 2", subject.getId(), author.getId()
        );

        createPostService.execute(request1);
        createPostService.execute(request2);

        mockMvc.perform(get("/api/posts/user/{userId}", subscriber.getId())
            .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(subscriber.getUserName()))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Post 1"))
            .andExpect(jsonPath("$[1].title").value("Post 2"));
    }

    @Test
    @WithMockUser(username = "subscriber", roles = {"USER"})
    void createPost_withInvalidData_shouldReturnBadRequest() throws Exception {
        CreatePostRequest invalidRequest = new CreatePostRequest(
                "", "Content", subject.getId(), author.getId()
        );

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
