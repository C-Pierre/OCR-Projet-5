package com.openclassrooms.mddapi.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.mddapi.user.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.user.service.GetUserService;
import com.openclassrooms.mddapi.user.service.UpdateUserService;
import com.openclassrooms.mddapi.user.request.UpdateUserRequest;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getUserService.execute(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(updateUserService.execute(id, request));
    }
}
