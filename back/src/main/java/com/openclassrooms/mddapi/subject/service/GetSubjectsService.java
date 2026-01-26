package com.openclassrooms.mddapi.subject.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.subject.mapper.SubjectMapper;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;

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

    public List<SubjectDto> execute() {
        return subjectPort.findAll()
            .stream()
            .map(subjectMapper::toDto)
            .toList();
    }
}
