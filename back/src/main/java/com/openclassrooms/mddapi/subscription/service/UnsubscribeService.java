package com.openclassrooms.mddapi.subscription.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subscription.model.Subscription;
import com.openclassrooms.mddapi.common.exception.BadRequestException;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;

@Service
public class UnsubscribeService {

    private final SubscriptionRepository subscriptionRepository;

    public UnsubscribeService(
        SubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void execute(Long subjectId, Long userId) {
        Subscription subscription = subscriptionRepository
            .findByUserIdAndSubjectId(userId, subjectId)
            .orElseThrow(() -> new BadRequestException("L'utilisateur n'est pas abonné à ce sujet."));

        subscriptionRepository.delete(subscription);
    }
}
