package com.openclassrooms.mddapi.subject.dto;

public class SubjectWithSubscriptionDto {

    private final Long id;
    private final String name;
    private final String description;
    private final boolean subscribed;

    public SubjectWithSubscriptionDto(
            Long id,
            String name,
            String description,
            boolean subscribed
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subscribed = subscribed;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isSubscribed() { return subscribed; }
}