package com.openclassrooms.mddapi.infrastructure.common.authorization.request;

import com.openclassrooms.mddapi.infrastructure.common.authorization.Ownable;

public record AuthorizationRequest(
    Long ownerId,
    String resourceName
) implements Ownable {}
