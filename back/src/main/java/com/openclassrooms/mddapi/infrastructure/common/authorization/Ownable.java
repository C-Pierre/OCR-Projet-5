package com.openclassrooms.mddapi.infrastructure.common.authorization;

public interface Ownable {
    Long ownerId();
    String resourceName();
}
