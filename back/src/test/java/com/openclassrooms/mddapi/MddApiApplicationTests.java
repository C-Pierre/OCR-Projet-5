package com.openclassrooms.mddapi;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
class MddApiApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isTrue();
	}

	@Test
	void main_shouldStartApplication() {
		System.setProperty("server.port", "0");
		assertThatCode(() -> MddApiApplication.main(new String[]{}))
			.doesNotThrowAnyException();
	}
}
