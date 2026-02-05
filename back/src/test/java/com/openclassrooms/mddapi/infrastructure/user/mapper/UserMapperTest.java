package com.openclassrooms.mddapi.infrastructure.user.mapper;

import com.openclassrooms.mddapi.infrastructure.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toDto_shouldMapAllFields() {
        User user = new User("john@test.com", "john");
        user.setPassword("secret");

        UserDto dto = userMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isNull();
        assertThat(dto.userName()).isEqualTo("john");
        assertThat(dto.email()).isEqualTo("john@test.com");
    }

    @Test
    void toDto_shouldReturnNull_whenUserIsNull() {
        assertThat(userMapper.toDto(null)).isNull();
    }
}
