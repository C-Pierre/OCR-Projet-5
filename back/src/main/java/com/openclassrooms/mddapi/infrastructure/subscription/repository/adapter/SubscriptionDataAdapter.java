package com.openclassrooms.mddapi.infrastructure.subscription.repository.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;
import com.openclassrooms.mddapi.domain.subscription.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.infrastructure.subscription.repository.port.SubscriptionDataPort;

@Service
public class SubscriptionDataAdapter implements SubscriptionDataPort {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionDataAdapter(
        SubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> findByUserIdAndSubjectId(Long userId, Long subjectId) {
        return subscriptionRepository.findByUserIdAndSubjectId(userId, subjectId);
    }

    @Override
    public List<Subscription> findByUserId(Long userId) { return subscriptionRepository.findByUserId(userId); }

    @Override
    public void delete(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    @Override
    public void save(Subscription subscription) { subscriptionRepository.save(subscription); }
}

