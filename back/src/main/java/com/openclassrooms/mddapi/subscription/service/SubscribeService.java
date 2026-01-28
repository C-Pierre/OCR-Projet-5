package com.openclassrooms.mddapi.subscription.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.model.User;
import com.openclassrooms.mddapi.subject.model.Subject;
import com.openclassrooms.mddapi.subscription.model.Subscription;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.common.exception.BadRequestException;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;

@Service
public class SubscribeService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubjectDataPort subjectDataPort;
    private final UserDataPort userDataPort;

    public SubscribeService(
        SubscriptionRepository subscriptionRepository,
        SubjectDataPort subjectDataPort,
        UserDataPort userDataPort
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.subjectDataPort = subjectDataPort;
        this.userDataPort = userDataPort;
    }

    public void execute(Long subjectId, Long userId) {
        Subject subject = subjectDataPort.getById(subjectId);
        User user = userDataPort.getById(userId);

        if (subscriptionRepository.findByUserIdAndSubjectId(userId, subjectId).isPresent()) {
            throw new BadRequestException("L'utilisateur est déjà abonné à ce sujet.");
        }

        Subscription subscription = new Subscription(user, subject);
        subscriptionRepository.save(subscription);
    }
}
