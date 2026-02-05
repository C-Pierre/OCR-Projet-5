package com.openclassrooms.mddapi.infrastructure.subject.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;

class SubjectMapperTest {

    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);

    @Test
    void toDto_shouldMapSubjectToSubjectDto() {
        Subject subject = new Subject("Math", "Math description");

        SubjectDto dto = subjectMapper.toDto(subject);

        assertNotNull(dto);
        assertEquals(subject.getName(), dto.name());
        assertEquals(subject.getDescription(), dto.description());
        assertNull(dto.id(), "Id should be null because Subject id is not set yet");
    }

    @Test
    void toDto_shouldHandleNull() {
        SubjectDto dto = subjectMapper.toDto(null);
        assertNull(dto, "Mapping null subject should return null");
    }
}
