package com.openclassrooms.mddapi.subject.controller;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.subject.service.GetSubjectsService;
import com.openclassrooms.mddapi.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.subject.service.GetSubjectsWithSubscriptionService;

class SubjectControllerTest {

    private SubjectController subjectController;
    private GetSubjectsService getSubjectsService;
    private GetSubjectsWithSubscriptionService getSubjectsWithSubscriptionService;

    @BeforeEach
    void setUp() {
        getSubjectsService = mock(GetSubjectsService.class);
        getSubjectsWithSubscriptionService = mock(GetSubjectsWithSubscriptionService.class);
        subjectController = new SubjectController(getSubjectsWithSubscriptionService, getSubjectsService);
    }

    @Test
    void getAll_shouldReturnListOfSubjectDto() {
        SubjectDto dto = new SubjectDto(1L, "Math", "toto");
        when(getSubjectsService.execute()).thenReturn(List.of(dto));

        ResponseEntity<List<SubjectDto>> response = subjectController.getAll();

        assertThat(response.getBody()).isNotNull().hasSize(1);
        assertThat(response.getBody().getFirst().name()).isEqualTo("Math");
        verify(getSubjectsService).execute();
    }

    @Test
    void getAllSubjectsForUser_shouldReturnListOfSubjectWithSubscriptionDto() {
        Long userId = 1L;
        SubjectWithSubscriptionDto dto = new SubjectWithSubscriptionDto(
            1L, "Math", "toto", true
        );
        when(getSubjectsWithSubscriptionService.execute(eq(userId))).thenReturn(List.of(dto));

        ResponseEntity<List<SubjectWithSubscriptionDto>> response = subjectController.getAllSubjectsForUser(userId);

        assertThat(response.getBody()).isNotNull().hasSize(1);
        assertThat(response.getBody().getFirst().name()).isEqualTo("Math");
        assertThat(response.getBody().getFirst().subscribed()).isTrue();

        verify(getSubjectsWithSubscriptionService).execute(userId);
    }
}
