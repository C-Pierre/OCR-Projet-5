package com.openclassrooms.mddapi.infrastructure.subject.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

public interface SubjectDataPort {
    Subject getById(Long id) throws NotFoundException;
    List<Subject> findAll();
    List<SubjectWithSubscriptionDto> findAllWithSubscriptionForUser(Long userId);
}