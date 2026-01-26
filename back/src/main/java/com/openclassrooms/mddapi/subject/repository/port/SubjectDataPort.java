package com.openclassrooms.mddapi.subject.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.subject.model.Subject;

public interface SubjectDataPort {
    List<Subject> findAll();
}