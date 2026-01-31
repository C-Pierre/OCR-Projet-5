package com.openclassrooms.mddapi.subscription.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.subscription.repository.port.SubscriptionDataPort;

@Service
public class GetUserSubscriptionsService {

    private final SubscriptionDataPort subscriptionDataPort;
    private final UserDataPort userDataPort;

    public GetUserSubscriptionsService(
        SubscriptionDataPort subscriptionDataPort,
        UserDataPort userDataPort
    ) {
        this.subscriptionDataPort = subscriptionDataPort;
        this.userDataPort = userDataPort;
    }

    public List<SubjectDto> execute(Long userId) {
        User user = userDataPort.getById(userId);

        List<Subscription> subscriptions = subscriptionDataPort.findByUserId(user.getId());

        return subscriptions.stream()
            .map(sub -> new SubjectDto(
                sub.getSubject().getId(),
                sub.getSubject().getName(),
                sub.getSubject().getDescription()
            ))
            .collect(Collectors.toList());
    }
}
