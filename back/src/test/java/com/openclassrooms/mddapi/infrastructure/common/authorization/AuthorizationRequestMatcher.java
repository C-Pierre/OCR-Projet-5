package com.openclassrooms.mddapi.infrastructure.common.authorization;

import org.mockito.ArgumentMatcher;
import com.openclassrooms.mddapi.infrastructure.common.authorization.request.AuthorizationRequest;

public class AuthorizationRequestMatcher implements ArgumentMatcher<AuthorizationRequest> {
    private final Long expectedUserId;
    private final String expectedResourceType;

    public AuthorizationRequestMatcher(Long expectedUserId, String expectedResourceType) {
        this.expectedUserId = expectedUserId;
        this.expectedResourceType = expectedResourceType;
    }

    @Override
    public boolean matches(AuthorizationRequest argument) {
        return argument != null &&
            expectedUserId.equals(argument.ownerId()) &&
            expectedResourceType.equals(argument.resourceName());
    }
}