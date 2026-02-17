package com.openclassrooms.mddapi.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;
import com.openclassrooms.mddapi.api.subscription.request.SubscribeRequest;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.BadRequestException;
import com.openclassrooms.mddapi.infrastructure.subscription.repository.port.SubscriptionDataPort;

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

    @CacheEvict(value = {"userSubscriptionsCache"}, allEntries = true)
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
