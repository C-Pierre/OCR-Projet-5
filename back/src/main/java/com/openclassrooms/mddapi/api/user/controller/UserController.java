package com.openclassrooms.mddapi.api.user.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import com.openclassrooms.mddapi.api.user.request.UpdateUserRequest;
import com.openclassrooms.mddapi.application.user.service.GetUserService;
import com.openclassrooms.mddapi.application.user.service.UpdateUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetUserService getUserService;
    private final UpdateUserService updateUserService;

    public UserController(
        GetUserService getUserService,
        UpdateUserService updateUserService
    ) {
        this.getUserService = getUserService;
        this.updateUserService = updateUserService;
    }

    @Operation(summary = "Get user by ID", description = "Returns a single user by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@userAuthorization.canGet(#id)")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getUserService.execute(id));
    }

    @Operation(summary = "Update user", description = "Updates the details of an existing user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@userAuthorization.canUpdate(#id)")
    public ResponseEntity<UserDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(updateUserService.execute(id, request));
    }
}
