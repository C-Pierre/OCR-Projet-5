package com.openclassrooms.mddapi.user.controller;

import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import com.openclassrooms.mddapi.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.mddapi.user.service.GetUserService;
import com.openclassrooms.mddapi.user.request.UpdateUserRequest;
import com.openclassrooms.mddapi.user.service.UpdateUserService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private GetUserService getUserService;
    private UpdateUserService updateUserService;
    private ObjectMapper objectMapper;
    private UserController userController;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        getUserService = Mockito.mock(GetUserService.class);
        updateUserService = Mockito.mock(UpdateUserService.class);
        objectMapper = new ObjectMapper();

        userController = new UserController(getUserService, updateUserService);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDto = new UserDto(1L, "john", "john@test.com", null, null);
    }

    @Test
    void findById_shouldReturnUser() throws Exception {
        Mockito.when(getUserService.execute(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userName").value("john"))
            .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("johnny", "johnny@test.com", "secret123");
        UserDto updatedDto = new UserDto(1L, "johnny", "johnny@test.com", null, null);

        Mockito.when(updateUserService.execute(eq(1L), any(UpdateUserRequest.class)))
            .thenReturn(updatedDto);

        mockMvc.perform(put("/api/users/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userName").value("johnny"))
            .andExpect(jsonPath("$.email").value("johnny@test.com"));
    }
}
