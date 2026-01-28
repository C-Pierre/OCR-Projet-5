package com.openclassrooms.mddapi.subject.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;

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

    public List<SubjectWithSubscriptionDto> execute(Long userId) {
        userDataPort.getById(userId);
        return subjectDataPort.findAllWithSubscriptionForUser(userId);
    }
}