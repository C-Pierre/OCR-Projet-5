package com.openclassrooms.mddapi.infrastructure.common.authorization.policy;

import java.util.EnumSet;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Ownable;
import com.openclassrooms.mddapi.application.user.service.GetCurrentUserService;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.ForbiddenException;

public abstract class AbstractOwnerShipPolicy implements AuthorizationPolicy {

    protected final GetCurrentUserService getCurrentUserService;

    protected AbstractOwnerShipPolicy(GetCurrentUserService getCurrentUserService) {
        this.getCurrentUserService = getCurrentUserService;
    }

    protected abstract EnumSet<Action> supportedActions();

    protected abstract String errorMessage(Ownable resource);

    @Override
    public boolean supports(Action action, Object resource) {
        return resource instanceof Ownable
            && supportedActions().contains(action);
    }

    @Override
    public void check(Object resource) {
        Ownable ownable = (Ownable) resource;
        Long currentUserId = getCurrentUserService.execute().id();

        if (!currentUserId.equals(ownable.ownerId())) {
            throw new ForbiddenException(errorMessage(ownable));
        }
    }
}
