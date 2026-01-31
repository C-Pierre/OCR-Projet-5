package com.openclassrooms.mddapi.common.authorization.request;

import com.openclassrooms.mddapi.common.authorization.Ownable;

public record AuthorizationRequest(
    Long ownerId,
    String resourceName
) implements Ownable {}
