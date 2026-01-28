package com.openclassrooms.mddapi.subject.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.subject.model.Subject;
import com.openclassrooms.mddapi.common.exception.NotFoundException;

public interface SubjectDataPort {
    Subject getById(Long id) throws NotFoundException;
    List<Subject> findAll();
}