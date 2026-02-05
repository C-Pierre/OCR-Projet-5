package com.openclassrooms.mddapi.api.subscription.controller;

import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.api.subscription.request.SubscribeRequest;
import com.openclassrooms.mddapi.application.subscription.service.SubscribeService;
import com.openclassrooms.mddapi.api.subscription.request.UnsubscribeRequest;
import com.openclassrooms.mddapi.application.subscription.service.UnsubscribeService;
import com.openclassrooms.mddapi.application.subscription.service.GetUserSubscriptionsService;

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

    @Operation(
        summary = "Get all Subscriptions for User",
        description = "Returns all subscriptions for user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: JWT token missing or invalid")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("@subscriptionAuthorization.canGet(#userId)")
    public ResponseEntity<List<SubjectDto>> getSubscriptionsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getUserSubscriptionsService.execute(userId));
    }

    @Operation(
        summary = "Create subscription : Subscribe user to a subject",
        description = "Creates a new subscription"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid subscription data")
    })
    @PostMapping
    @PreAuthorize("@subscriptionAuthorization.canSubscribe(#request.userId)")
    public ResponseEntity<Void> subscribe(@Valid @RequestBody SubscribeRequest request) {
        subscribeService.execute(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unsubscribe : Delete subscription", description = "Deletes a subscription by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @DeleteMapping
    @PreAuthorize("@subscriptionAuthorization.canUnsubscribe(#request.userId)")
    public ResponseEntity<Void> unsubscribe(@Valid @RequestBody UnsubscribeRequest request) {
        unsubscribeService.execute(request);
        return ResponseEntity.ok().build();
    }
}