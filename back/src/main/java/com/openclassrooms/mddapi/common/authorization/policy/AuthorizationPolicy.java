package com.openclassrooms.mddapi.common.authorization.policy;

import com.openclassrooms.mddapi.common.authorization.Action;

public interface AuthorizationPolicy {

    boolean supports(Action action, Object resource);

    void check(Object resource);
}