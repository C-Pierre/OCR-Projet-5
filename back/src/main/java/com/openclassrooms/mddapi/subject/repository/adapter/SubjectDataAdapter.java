package com.openclassrooms.mddapi.subject.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subject.model.Subject;
import com.openclassrooms.mddapi.common.exception.NotFoundException;
import com.openclassrooms.mddapi.subject.repository.SubjectRepository;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;

@Service
public class SubjectDataAdapter implements SubjectDataPort {

    private final SubjectRepository subjectRepository;

    public SubjectDataAdapter(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
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

