package com.openclassrooms.mddapi.subject.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.subject.mapper.SubjectMapper;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;

@Service
public class GetSubjectService {

    private final SubjectMapper subjectMapper;
    private final SubjectDataPort subjectPort;

    public GetSubjectService(
            SubjectMapper subjectMapper,
            SubjectDataPort subjectPort
    ) {
        this.subjectMapper = subjectMapper;
        this.subjectPort = subjectPort;
    }

    public SubjectDto execute(Long id) {
        return subjectMapper.toDto(subjectPort.getById(id));
    }
}
