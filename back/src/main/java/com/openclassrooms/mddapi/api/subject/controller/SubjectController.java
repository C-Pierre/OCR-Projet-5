package com.openclassrooms.mddapi.api.subject.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.application.subject.service.GetSubjectsService;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import com.openclassrooms.mddapi.application.subject.service.GetSubjectsWithSubscriptionService;

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

    @Operation(summary = "Get all subjects", description = "Returns all subjects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
    })
    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAll() {
        return ResponseEntity.ok(getSubjectsService.execute());
    }

    @Operation(
        summary = "Get all subjects with user subscriptions",
        description = "Returns all subjects with user subscriptions"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: JWT token missing or invalid")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("@subscriptionAuthorization.canGet(#userId)")
    public ResponseEntity<List<SubjectWithSubscriptionDto>> getAllSubjectsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getSubjectsWithSubscriptionService.execute(userId));
    }
}