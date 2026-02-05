package com.openclassrooms.mddapi.application.subject.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.infrastructure.subject.mapper.SubjectMapper;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;

@Service
public class GetSubjectsService {

    private final SubjectMapper subjectMapper;
    private final SubjectDataPort subjectPort;

    public GetSubjectsService(
            SubjectMapper subjectMapper,
            SubjectDataPort subjectPort
    ) {
        this.subjectMapper = subjectMapper;
        this.subjectPort = subjectPort;
    }

    @Cacheable("subjectsCache")
    public List<SubjectDto> execute() {
        return subjectPort.findAll()
            .stream()
            .map(subjectMapper::toDto)
            .toList();
    }
}
