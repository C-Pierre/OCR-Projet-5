package com.openclassrooms.mddapi.subscription.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.subscription.service.SubscribeService;
import com.openclassrooms.mddapi.subscription.service.UnsubscribeService;
import com.openclassrooms.mddapi.subscription.service.GetUserSubscriptionsService;

@RestController
@RequestMapping("/api/subscribtions")
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
    public ResponseEntity<List<SubjectDto>> getSubscriptionsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getUserSubscriptionsService.execute(userId));
    }

    @PostMapping
    public ResponseEntity<Void> subscribe(@RequestParam Long userId, @RequestParam Long subjectId) {
        subscribeService.execute(subjectId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unsubscribe(@RequestParam Long userId, @RequestParam Long subjectId) {
        unsubscribeService.execute(subjectId, userId);
        return ResponseEntity.ok().build();
    }
}