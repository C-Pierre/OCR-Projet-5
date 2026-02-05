package com.openclassrooms.mddapi.application.user.service;

import com.openclassrooms.mddapi.application.user.service.GetUserService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.infrastructure.user.mapper.UserMapper;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;

class GetUserServiceTest {

    private GetUserService service;
    private UserDataPort userDataPort;
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        userDataPort = mock(UserDataPort.class);
        mapper = mock(UserMapper.class);
        service = new GetUserService(mapper, userDataPort);
    }

    @Test
    void execute_shouldReturnUserDto_whenUserExists() {
        User user = new User("john@test.com", "john");
        UserDto dto = new UserDto(1L, "john", "john@test.com", null, null);

        when(userDataPort.getById(1L)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(dto);

        UserDto result = service.execute(1L);

        assertNotNull(result);
        assertEquals("john", result.userName());
        assertEquals("john@test.com", result.email());

        verify(userDataPort).getById(1L);
        verify(mapper).toDto(user);
    }

    @Test
    void execute_shouldReturnNull_whenUserNotFound() {
        when(userDataPort.getById(99L)).thenReturn(null);
        when(mapper.toDto(null)).thenReturn(null);

        UserDto result = service.execute(99L);

        assertNull(result);

        verify(userDataPort).getById(99L);
        verify(mapper).toDto(null);
    }
}
