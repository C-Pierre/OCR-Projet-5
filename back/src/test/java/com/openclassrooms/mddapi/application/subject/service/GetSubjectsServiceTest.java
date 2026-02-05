package com.openclassrooms.mddapi.application.subject.service;

import java.util.List;

import com.openclassrooms.mddapi.application.subject.service.GetSubjectsService;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.infrastructure.subject.mapper.SubjectMapper;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;

class GetSubjectsServiceTest {

    private SubjectDataPort subjectPort;
    private SubjectMapper subjectMapper;
    private GetSubjectsService getSubjectsService;

    @BeforeEach
    void setUp() {
        subjectPort = Mockito.mock(SubjectDataPort.class);
        subjectMapper = Mockito.mock(SubjectMapper.class);
        getSubjectsService = new GetSubjectsService(subjectMapper, subjectPort);
    }

    @Test
    void execute_shouldReturnMappedSubjects() {
        Subject subject1 = new Subject("Math", "Math description");
        Subject subject2 = new Subject("Physics", "Physics description");

        SubjectDto dto1 = new SubjectDto(1L, "Math", "Math description");
        SubjectDto dto2 = new SubjectDto(2L, "Physics", "Physics description");

        when(subjectPort.findAll()).thenReturn(List.of(subject1, subject2));
        when(subjectMapper.toDto(subject1)).thenReturn(dto1);
        when(subjectMapper.toDto(subject2)).thenReturn(dto2);

        List<SubjectDto> result = getSubjectsService.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).name());
        assertEquals("Physics", result.get(1).name());

        verify(subjectPort).findAll();
        verify(subjectMapper).toDto(subject1);
        verify(subjectMapper).toDto(subject2);
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoSubjects() {
        when(subjectPort.findAll()).thenReturn(List.of());

        List<SubjectDto> result = getSubjectsService.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(subjectPort).findAll();
        verifyNoInteractions(subjectMapper);
    }
}
