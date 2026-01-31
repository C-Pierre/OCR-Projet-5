package com.openclassrooms.mddapi.subscription.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.common.exception.BadRequestException;
import com.openclassrooms.mddapi.subscription.request.UnsubscribeRequest;
import com.openclassrooms.mddapi.subscription.repository.port.SubscriptionDataPort;

@Service
public class UnsubscribeService {

    private final SubscriptionDataPort subscriptionDataPort;

    public UnsubscribeService(SubscriptionDataPort subscriptionDataPort) {
        this.subscriptionDataPort = subscriptionDataPort;
    }

    public void execute(UnsubscribeRequest request) {
        Subscription subscription = subscriptionDataPort
            .findByUserIdAndSubjectId(request.userId(), request.subjectId())
            .orElseThrow(() -> new BadRequestException(
                "L'utilisateur n'est pas abonné à ce sujet."
            ));

        subscriptionDataPort.delete(subscription);
    }
}
