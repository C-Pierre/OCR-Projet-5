package com.openclassrooms.mddapi.user.controller;

import org.springframework.http.ResponseEntity;
import com.openclassrooms.mddapi.user.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.openclassrooms.mddapi.user.service.GetUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetUserService getUserService;

    public UserController(
        GetUserService getUserService
    ) {
        this.getUserService = getUserService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getUserService.execute(id));
    }
}
