package com.openclassrooms.mddapi.application.user.service;

import com.openclassrooms.mddapi.application.user.service.UpdateUserService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import com.openclassrooms.mddapi.infrastructure.user.mapper.UserMapper;
import com.openclassrooms.mddapi.api.user.request.UpdateUserRequest;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import org.springframework.security.crypto.password.PasswordEncoder;

class UpdateUserServiceTest {

    private UpdateUserService service;
    private UserDataPort userDataPort;
    private UserMapper mapper;
    private PasswordEncoder passwordEncoder;
    private User user;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        userDataPort = mock(UserDataPort.class);
        mapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new UpdateUserService(passwordEncoder, mapper, userDataPort);

        user = new User("john@test.com", "john");
        user.setPassword("oldPassword");

        dto = new UserDto(
            1L, "john", "john@test.com", null, null
        );
    }

    @Test
    void execute_shouldUpdateUserNameAndReturnDto() {
        UpdateUserRequest request = new UpdateUserRequest(
            "johnny", null, null
        );
        when(userDataPort.getById(1L)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(dto);

        UserDto result = service.execute(1L, request);

        assertEquals("john", result.userName());
        verify(userDataPort).save(user);
        verify(mapper).toDto(user);
        assertEquals("johnny", user.getUserName());
        assertEquals("john@test.com", user.getEmail());
    }

    @Test
    void execute_shouldUpdateEmailAndReturnDto() {
        UpdateUserRequest request = new UpdateUserRequest(
            null, "new@test.com", null
        );
        when(userDataPort.getById(1L)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(dto);

        UserDto result = service.execute(1L, request);

        assertEquals("john", result.userName());
        verify(userDataPort).save(user);
        assertEquals("new@test.com", user.getEmail());
    }

    @Test
    void execute_shouldUpdatePasswordAndReturnDto() {
        UpdateUserRequest request = new UpdateUserRequest(
            null, null, "newPassword"
        );
        when(userDataPort.getById(1L)).thenReturn(user);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(mapper.toDto(user)).thenReturn(dto);

        service.execute(1L, request);

        verify(userDataPort).save(user);
        verify(passwordEncoder).encode("newPassword");
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void execute_shouldNotSave_whenNothingModified() {
        UpdateUserRequest request = new UpdateUserRequest(
            "john", "john@test.com", null
        );
        when(userDataPort.getById(1L)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(dto);

        service.execute(1L, request);

        verify(userDataPort, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenUserNotFound() {
        when(userDataPort.getById(99L)).thenReturn(null);

        assertThrows(
            NullPointerException.class, () -> service.execute(
                99L, new UpdateUserRequest("a","a","a")
            )
        );
        verify(userDataPort).getById(99L);
        verify(mapper, never()).toDto(any());
    }
    @Test
    void execute_shouldNotUpdatePassword_whenPasswordIsNull() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, null);
        when(userDataPort.getById(1L)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(dto);

        service.execute(1L, request);

        verify(userDataPort, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void execute_shouldNotUpdatePassword_whenPasswordIsBlank() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, "   ");
        when(userDataPort.getById(1L)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(dto);

        service.execute(1L, request);

        verify(userDataPort, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

}
