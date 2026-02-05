package com.openclassrooms.mddapi.infrastructure.user.authorization;

import org.springframework.stereotype.Component;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;
import com.openclassrooms.mddapi.infrastructure.common.authorization.service.AuthorizationService;
import com.openclassrooms.mddapi.infrastructure.common.authorization.request.AuthorizationRequest;

@Component("userAuthorization")
public class UserAuthorization {

    private final AuthorizationService authorizationService;

    public UserAuthorization(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public boolean canGet(Long userId) {
        this.authorization(userId, Action.READ);
        return true;
    }

    public boolean canUpdate(Long userId) {
        this.authorization(userId, Action.SUBSCRIBE);
        return true;
    }

    private void authorization(Long userId, Action action) {
        authorizationService.authorize(
            new AuthorizationRequest(userId, "user"),
            action
        );
    }
}
