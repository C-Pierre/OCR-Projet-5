package com.openclassrooms.mddapi.common.authorization.policy;

import java.util.EnumSet;
import org.springframework.stereotype.Component;
import com.openclassrooms.mddapi.common.authorization.Action;
import com.openclassrooms.mddapi.common.authorization.Ownable;
import com.openclassrooms.mddapi.user.service.GetCurrentUserService;

@Component
public class UserShipPolicy extends AbstractOwnerShipPolicy {

    public UserShipPolicy(GetCurrentUserService getCurrentUserService) {
        super(getCurrentUserService);
    }

    @Override
    protected EnumSet<Action> supportedActions() {
        return EnumSet.of(Action.READ, Action.UPDATE, Action.DELETE);
    }

    @Override
    protected String errorMessage(Ownable resource) {
        return "Vous ne pouvez pas éditer cette ressource " + resource.resourceName();
    }
}
