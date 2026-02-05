package com.openclassrooms.mddapi.infrastructure.common.authorization.policy;

import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;

public interface AuthorizationPolicy {

    boolean supports(Action action, Object resource);

    void check(Object resource);
}