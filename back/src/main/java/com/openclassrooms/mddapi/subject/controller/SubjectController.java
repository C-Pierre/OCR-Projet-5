package com.openclassrooms.mddapi.subject.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.subject.service.GetSubjectsService;
import com.openclassrooms.mddapi.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.subject.service.GetSubjectsWithSubscriptionService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final GetSubjectsWithSubscriptionService getSubjectsWithSubscriptionService;
    private final GetSubjectsService getSubjectsService;

    public SubjectController(
            GetSubjectsWithSubscriptionService getSubjectsWithSubscriptionService,
            GetSubjectsService getSubjectsService
    ) {
        this.getSubjectsWithSubscriptionService = getSubjectsWithSubscriptionService;
        this.getSubjectsService = getSubjectsService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAll() {
        return ResponseEntity.ok(getSubjectsService.execute());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubjectWithSubscriptionDto>> getAllSubjectsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getSubjectsWithSubscriptionService.execute(userId));
    }
}