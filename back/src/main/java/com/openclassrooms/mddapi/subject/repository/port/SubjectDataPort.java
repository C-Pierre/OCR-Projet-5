package com.openclassrooms.mddapi.subject.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.common.exception.NotFoundException;
import com.openclassrooms.mddapi.subject.dto.SubjectWithSubscriptionDto;

public interface SubjectDataPort {
    Subject getById(Long id) throws NotFoundException;
    List<Subject> findAll();
    List<SubjectWithSubscriptionDto> findAllWithSubscriptionForUser(Long userId);
}