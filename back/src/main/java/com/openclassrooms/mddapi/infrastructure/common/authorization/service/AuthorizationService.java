package com.openclassrooms.mddapi.infrastructure.common.authorization.service;

import java.util.List;

import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;
import com.openclassrooms.mddapi.infrastructure.common.authorization.policy.AuthorizationPolicy;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final List<AuthorizationPolicy> policies;

    public AuthorizationService(List<AuthorizationPolicy> policies) {
        this.policies = policies;
    }

    public void authorize(Object resource, Action action) {
        policies.stream()
            .filter(policy -> policy.supports(action, resource))
            .findFirst()
            .orElseThrow(() ->
                new IllegalStateException("No policy for " + action))
            .check(resource);
    }
}
