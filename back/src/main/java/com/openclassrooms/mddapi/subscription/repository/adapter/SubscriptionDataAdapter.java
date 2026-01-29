package com.openclassrooms.mddapi.subscription.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.common.exception.NotFoundException;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.subscription.repository.port.SubscriptionDataPort;

@Service
public class SubscriptionDataAdapter implements SubscriptionDataPort {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionDataAdapter(
        SubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription findByUserIdAndSubjectId(Long userId, Long subjectId) {
        return subscriptionRepository.findByUserIdAndSubjectId(userId, subjectId)
            .orElseThrow(() -> new NotFoundException(
                "Subscription not found for userId: " + userId + " and subjectId: " + subjectId
            )
        );
    }

    @Override
    public List<Subscription> findByUserId(Long userId) { return subscriptionRepository.findByUserId(userId); }

    @Override
    public List<Subscription> findBySubjectId(Long subjectId) {
        return subscriptionRepository.findBySubjectId(subjectId);
    }

    @Override
    public void save(Subscription subscription) { subscriptionRepository.save(subscription); }
}

