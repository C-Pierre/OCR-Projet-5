package com.openclassrooms.mddapi.application.subject.service;

import java.util.List;

import com.openclassrooms.mddapi.application.subject.service.GetSubjectsWithSubscriptionService;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;

class GetSubjectsWithSubscriptionServiceTest {

    private SubjectDataPort subjectDataPort;
    private UserDataPort userDataPort;
    private GetSubjectsWithSubscriptionService service;

    @BeforeEach
    void setUp() {
        subjectDataPort = Mockito.mock(SubjectDataPort.class);
        userDataPort = Mockito.mock(UserDataPort.class);
        service = new GetSubjectsWithSubscriptionService(subjectDataPort, userDataPort);
    }

    @Test
    void execute_shouldReturnSubjectsWithSubscriptionForUser() {
        Long userId = 1L;

        // Préparer les DTO simulés
        SubjectWithSubscriptionDto dto1 = new SubjectWithSubscriptionDto(1L, "Math", "Math description", true);
        SubjectWithSubscriptionDto dto2 = new SubjectWithSubscriptionDto(2L, "Physics", "Physics description", false);

        when(subjectDataPort.findAllWithSubscriptionForUser(userId)).thenReturn(List.of(dto1, dto2));

        List<SubjectWithSubscriptionDto> result = service.execute(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).name());
        assertTrue(result.get(0).subscribed());
        assertEquals("Physics", result.get(1).name());
        assertFalse(result.get(1).subscribed());

        // Vérifier que getById est appelé pour valider l'utilisateur
        verify(userDataPort).getById(userId);
        verify(subjectDataPort).findAllWithSubscriptionForUser(userId);
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoSubjects() {
        Long userId = 42L;

        when(subjectDataPort.findAllWithSubscriptionForUser(userId)).thenReturn(List.of());

        List<SubjectWithSubscriptionDto> result = service.execute(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userDataPort).getById(userId);
        verify(subjectDataPort).findAllWithSubscriptionForUser(userId);
    }
}
