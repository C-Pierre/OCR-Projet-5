package com.openclassrooms.mddapi.subject.repository.adapter;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.openclassrooms.mddapi.subject.repository.SubjectRepository;
import com.openclassrooms.mddapi.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.common.exception.type.NotFoundException;

class SubjectDataAdapterTest {

    private SubjectRepository subjectRepository;
    private SubjectDataAdapter subjectDataAdapter;

    @BeforeEach
    void setUp() {
        subjectRepository = mock(SubjectRepository.class);
        subjectDataAdapter = new SubjectDataAdapter(subjectRepository);
    }

    @Test
    void findAllWithSubscriptionForUser_shouldDelegateToRepository() {
        Long userId = 1L;
        List<SubjectWithSubscriptionDto> list = List.of(
            new SubjectWithSubscriptionDto(1L, "Math", "Description", true)
        );
        when(subjectRepository.findAllWithSubscriptionForUser(userId)).thenReturn(list);

        List<SubjectWithSubscriptionDto> result = subjectDataAdapter.findAllWithSubscriptionForUser(userId);

        assertThat(result).isSameAs(list);
        verify(subjectRepository, times(1)).findAllWithSubscriptionForUser(userId);
    }

    @Test
    void getById_shouldReturnSubject_whenExists() {
        Subject subject = new Subject("Math", "Description");
        Long id = subject.getId();
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        Subject result = subjectDataAdapter.getById(id);

        assertThat(result).isSameAs(subject);
        verify(subjectRepository, times(1)).findById(id);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenNotExists() {
        when(subjectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectDataAdapter.getById(999L));
        verify(subjectRepository, times(1)).findById(999L);
    }

    @Test
    void findAll_shouldDelegateToRepository() {
        List<Subject> subjects = List.of(new Subject("Math", "Desc"));
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<Subject> result = subjectDataAdapter.findAll();

        assertThat(result).isSameAs(subjects);
        verify(subjectRepository, times(1)).findAll();
    }
}
