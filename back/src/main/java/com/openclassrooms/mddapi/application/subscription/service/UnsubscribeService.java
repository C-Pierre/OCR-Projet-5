package com.openclassrooms.mddapi.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;
import com.openclassrooms.mddapi.api.subscription.request.UnsubscribeRequest;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.BadRequestException;
import com.openclassrooms.mddapi.infrastructure.subscription.repository.port.SubscriptionDataPort;

@Service
public class UnsubscribeService {

    private final SubscriptionDataPort subscriptionDataPort;

    public UnsubscribeService(SubscriptionDataPort subscriptionDataPort) {
        this.subscriptionDataPort = subscriptionDataPort;
    }

    @Caching(evict = {
        @CacheEvict(value = "userSubscriptionsCache", allEntries = true),
        @CacheEvict(value = "userSubscriptionCache", key = "#id")
    })
    public void execute(UnsubscribeRequest request) {
        Subscription subscription = subscriptionDataPort
            .findByUserIdAndSubjectId(request.userId(), request.subjectId())
            .orElseThrow(() -> new BadRequestException(
                "L'utilisateur n'est pas abonné à ce sujet."
            ));

        subscriptionDataPort.delete(subscription);
    }
}
