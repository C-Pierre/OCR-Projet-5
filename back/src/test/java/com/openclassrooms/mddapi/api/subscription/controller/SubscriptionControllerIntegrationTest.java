package com.openclassrooms.mddapi.api.subscription.controller;

import java.util.List;
import java.util.Arrays;

import com.openclassrooms.mddapi.api.subscription.controller.SubscriptionController;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.eq;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.openclassrooms.mddapi.application.subscription.service.SubscribeService;
import com.openclassrooms.mddapi.api.subscription.request.SubscribeRequest;
import com.openclassrooms.mddapi.application.subscription.service.UnsubscribeService;
import com.openclassrooms.mddapi.api.subscription.request.UnsubscribeRequest;
import com.openclassrooms.mddapi.application.subscription.service.GetUserSubscriptionsService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerIntegrationTest {

    private MockMvc mockMvc;
    private GetUserSubscriptionsService getUserSubscriptionsService;
    private SubscribeService subscribeService;
    private UnsubscribeService unsubscribeService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        getUserSubscriptionsService = Mockito.mock(GetUserSubscriptionsService.class);
        subscribeService = Mockito.mock(SubscribeService.class);
        unsubscribeService = Mockito.mock(UnsubscribeService.class);

        SubscriptionController subscriptionController = new SubscriptionController(
            getUserSubscriptionsService, subscribeService, unsubscribeService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getSubscriptionsForUser_shouldReturnList() throws Exception {
        Long userId = 1L;
        List<SubjectDto> subjects = Arrays.asList(
            new SubjectDto(1L, "Java", "Description"),
            new SubjectDto(2L, "Spring", "Description")
        );

        Mockito.when(getUserSubscriptionsService.execute(userId)).thenReturn(subjects);

        mockMvc.perform(get("/api/subscriptions/user/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Java"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Spring"));
    }

    @Test
    void subscribe_shouldCallServiceAndReturnOk() throws Exception {
        SubscribeRequest request = new SubscribeRequest(1L, 1L);

        mockMvc.perform(post("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        Mockito.verify(subscribeService, Mockito.times(1))
            .execute(eq(request));
    }

    @Test
    void unsubscribe_shouldCallServiceAndReturnOk() throws Exception {
        UnsubscribeRequest request = new UnsubscribeRequest(1L, 1L);

        mockMvc.perform(delete("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        Mockito.verify(unsubscribeService, Mockito.times(1))
            .execute(eq(request));
    }
}
