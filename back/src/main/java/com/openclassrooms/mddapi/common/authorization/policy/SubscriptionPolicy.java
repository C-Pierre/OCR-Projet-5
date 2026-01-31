package com.openclassrooms.mddapi.common.authorization.policy;

import org.springframework.stereotype.Component;
import com.openclassrooms.mddapi.common.authorization.Action;
import com.openclassrooms.mddapi.common.authorization.Ownable;
import com.openclassrooms.mddapi.user.service.GetCurrentUserService;

import java.util.EnumSet;

@Component
public class SubscriptionPolicy extends AbstractOwnerShipPolicy {

    public SubscriptionPolicy(GetCurrentUserService getCurrentUserService) {
        super(getCurrentUserService);
    }

    @Override
    protected EnumSet<Action> supportedActions() {
        return EnumSet.of(Action.SUBSCRIBE, Action.UNSUBSCRIBE);
    }

    @Override
    protected String errorMessage(Ownable resource) {
        return "Vous ne pouvez pas éditer cette ressource " + resource.resourceName();
    }
}