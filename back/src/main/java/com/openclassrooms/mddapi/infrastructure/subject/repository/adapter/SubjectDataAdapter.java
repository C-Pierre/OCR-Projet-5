package com.openclassrooms.mddapi.infrastructure.subject.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

@Service
public class SubjectDataAdapter implements SubjectDataPort {

    private final SubjectRepository subjectRepository;

    public SubjectDataAdapter(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectWithSubscriptionDto> findAllWithSubscriptionForUser(Long userId) {
        return subjectRepository.findAllWithSubscriptionForUser(userId);
    }

    @Override
    public Subject getById(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }
}

