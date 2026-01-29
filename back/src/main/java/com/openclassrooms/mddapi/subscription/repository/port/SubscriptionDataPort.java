package com.openclassrooms.mddapi.subscription.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.common.exception.NotFoundException;

public interface SubscriptionDataPort {
    Subscription findByUserIdAndSubjectId(Long userId, Long subjectId) throws NotFoundException;
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findBySubjectId(Long subjectId);
    void save(Subscription subscription);
}