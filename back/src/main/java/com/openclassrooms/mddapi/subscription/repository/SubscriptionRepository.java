package com.openclassrooms.mddapi.subscription.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.mddapi.subscription.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndSubjectId(Long userId, Long subjectId);
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findBySubjectId(Long subjectId);
}
