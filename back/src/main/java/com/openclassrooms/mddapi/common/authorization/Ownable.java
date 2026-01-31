package com.openclassrooms.mddapi.common.authorization;

public interface Ownable {
    Long ownerId();
    String resourceName();
}
