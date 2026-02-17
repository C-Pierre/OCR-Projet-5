package com.openclassrooms.mddapi.infrastructure.subscription.repository.port;

import java.util.List;
import java.util.Optional;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;

public interface SubscriptionDataPort {
    Optional<Subscription> findByUserIdAndSubjectId(Long userId, Long subjectId);
    List<Subscription> findByUserId(Long userId);
    void save(Subscription subscription);
    void delete(Subscription subscription);
}
