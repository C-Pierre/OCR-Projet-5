package com.openclassrooms.mddapi.api.subject.controller;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.application.subject.service.GetSubjectsService;
import org.springframework.security.test.context.support.WithMockUser;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.openclassrooms.mddapi.application.subject.service.GetSubjectsWithSubscriptionService;
import com.openclassrooms.mddapi.infrastructure.subscription.authorization.SubscriptionAuthorization;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SubjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetSubjectsService getSubjectsService;

    @MockBean
    private GetSubjectsWithSubscriptionService getSubjectsWithSubscriptionService;

    @MockBean(name = "subscriptionAuthorization")
    private SubscriptionAuthorization subscriptionAuthorization;

    @Test
    void getAll_shouldReturnListOfSubjects() throws Exception {
        SubjectDto dto = new SubjectDto(1L, "Math", "Test");
        when(getSubjectsService.execute()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/subjects")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Math"))
            .andExpect(jsonPath("$[0].description").value("Test"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllSubjectsForUser_shouldReturnListOfSubjectsWithSubscription() throws Exception {
        Long userId = 1L;
        SubjectWithSubscriptionDto dto = new SubjectWithSubscriptionDto(1L, "Math", "Test", true);

        when(getSubjectsWithSubscriptionService.execute(eq(userId))).thenReturn(List.of(dto));
        when(subscriptionAuthorization.canGet(anyLong())).thenReturn(true); // <-- important

        mockMvc.perform(get("/api/subjects/user/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Math"))
            .andExpect(jsonPath("$[0].description").value("Test"))
            .andExpect(jsonPath("$[0].subscribed").value(true));
    }
}
