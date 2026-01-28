package com.openclassrooms.mddapi.subscription.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.model.User;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.subscription.model.Subscription;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;

@Service
public class GetUserSubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserDataPort userDataPort;

    public GetUserSubscriptionsService(
        SubscriptionRepository subscriptionRepository,
        UserDataPort userDataPort
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.userDataPort = userDataPort;
    }

    public List<SubjectDto> execute(Long userId) {
        User user = userDataPort.getById(userId);

        List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());

        return subscriptions.stream()
            .map(sub -> new SubjectDto(
                sub.getSubject().getId(),
                sub.getSubject().getName(),
                sub.getSubject().getDescription()
            ))
            .collect(Collectors.toList());
    }
}
