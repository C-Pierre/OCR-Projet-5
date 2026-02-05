package com.openclassrooms.mddapi.infrastructure.subscription.authorization;

import org.springframework.stereotype.Component;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;
import com.openclassrooms.mddapi.infrastructure.common.authorization.service.AuthorizationService;
import com.openclassrooms.mddapi.infrastructure.common.authorization.request.AuthorizationRequest;

@Component("subscriptionAuthorization")
public class SubscriptionAuthorization {

    private final AuthorizationService authorizationService;

    public SubscriptionAuthorization(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public boolean canGet(Long userId) {
        this.authorization(userId, Action.READ);
        return true;
    }

    public boolean canSubscribe(Long userId) {
        this.authorization(userId, Action.SUBSCRIBE);
        return true;
    }

    public boolean canUnsubscribe(Long userId) {
        this.authorization(userId, Action.UNSUBSCRIBE);
        return true;
    }

    private void authorization(Long userId, Action action) {
        authorizationService.authorize(
            new AuthorizationRequest(userId, "subscription"),
            action
        );
    }
}
