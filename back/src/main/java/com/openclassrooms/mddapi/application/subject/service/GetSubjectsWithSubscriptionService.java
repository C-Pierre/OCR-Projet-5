package com.openclassrooms.mddapi.application.subject.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;

@Service
public class GetSubjectsWithSubscriptionService {

    private final SubjectDataPort subjectDataPort;
    private final UserDataPort userDataPort;

    public GetSubjectsWithSubscriptionService(
        SubjectDataPort subjectDataPort,
        UserDataPort userDataPort
    ) {
        this.subjectDataPort = subjectDataPort;
        this.userDataPort = userDataPort;
    }

    @Cacheable("subjectsWithDescriptionCache")
    public List<SubjectWithSubscriptionDto> execute(Long userId) {
        userDataPort.getById(userId);
        return subjectDataPort.findAllWithSubscriptionForUser(userId);
    }
}