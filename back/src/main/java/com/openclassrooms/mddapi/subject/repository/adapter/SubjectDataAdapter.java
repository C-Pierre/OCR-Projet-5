package com.openclassrooms.mddapi.subject.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subject.model.Subject;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;
import com.openclassrooms.mddapi.subject.repository.SubjectRepository;

@Service
public class SubjectDataAdapter implements SubjectDataPort {

    private final SubjectRepository subjectRepository;

    public SubjectDataAdapter(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }
}

