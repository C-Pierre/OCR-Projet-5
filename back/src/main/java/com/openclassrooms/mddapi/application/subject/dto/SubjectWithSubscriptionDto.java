package com.openclassrooms.mddapi.application.subject.dto;

public record SubjectWithSubscriptionDto(
        Long id,
        String name,
        String description,
        boolean subscribed
) {}
