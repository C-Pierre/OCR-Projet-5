package com.openclassrooms.mddapi.subscription.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import org.springframework.security.access.prepost.PreAuthorize;
import com.openclassrooms.mddapi.subscription.request.SubscribeRequest;
import com.openclassrooms.mddapi.subscription.service.SubscribeService;
import com.openclassrooms.mddapi.subscription.request.UnsubscribeRequest;
import com.openclassrooms.mddapi.subscription.service.UnsubscribeService;
import com.openclassrooms.mddapi.subscription.service.GetUserSubscriptionsService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final GetUserSubscriptionsService getUserSubscriptionsService;
    private final SubscribeService subscribeService;
    private final UnsubscribeService unsubscribeService;

    public SubscriptionController(
            GetUserSubscriptionsService getUserSubscriptionsService,
            SubscribeService subscribeService,
            UnsubscribeService unsubscribeService
    ) {
        this.getUserSubscriptionsService = getUserSubscriptionsService;
        this.subscribeService = subscribeService;
        this.unsubscribeService = unsubscribeService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@subscriptionAuthorization.canGet(#userId)")
    public ResponseEntity<List<SubjectDto>> getSubscriptionsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getUserSubscriptionsService.execute(userId));
    }

    @PostMapping
    @PreAuthorize("@subscriptionAuthorization.canSubscribe(#request.userId)")
    public ResponseEntity<Void> subscribe(@Valid @RequestBody SubscribeRequest request) {
        subscribeService.execute(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("@subscriptionAuthorization.canUnsubscribe(#request.userId)")
    public ResponseEntity<Void> unsubscribe(@Valid @RequestBody UnsubscribeRequest request) {
        unsubscribeService.execute(request);
        return ResponseEntity.ok().build();
    }
}