package com.openclassrooms.mddapi.subject.dto;

public record SubjectWithSubscriptionDto(
        Long id,
        String name,
        String description,
        boolean subscribed
) {}
