package com.openclassrooms.mddapi.subscription.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.common.exception.BadRequestException;
import com.openclassrooms.mddapi.subscription.request.SubscribeRequest;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;
import com.openclassrooms.mddapi.subscription.repository.port.SubscriptionDataPort;

@Service
public class SubscribeService {

    private final SubscriptionDataPort subscriptionDataPort;
    private final SubjectDataPort subjectDataPort;
    private final UserDataPort userDataPort;

    public SubscribeService(
        SubscriptionDataPort subscriptionDataPort,
        SubjectDataPort subjectDataPort,
        UserDataPort userDataPort
    ) {
        this.subscriptionDataPort = subscriptionDataPort;
        this.subjectDataPort = subjectDataPort;
        this.userDataPort = userDataPort;
    }

    public void execute(SubscribeRequest request) {
        Subject subject = subjectDataPort.getById(request.subjectId());
        User user = userDataPort.getById(request.userId());

        boolean alreadySubscribed = subscriptionDataPort
            .findByUserIdAndSubjectId(request.userId(), request.subjectId())
            .isPresent();

        if (alreadySubscribed) {
            throw new BadRequestException("L'utilisateur est déjà abonné à ce sujet.");
        }

        Subscription subscription = new Subscription(user, subject);
        subscriptionDataPort.save(subscription);
    }
}
