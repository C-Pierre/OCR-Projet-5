package com.openclassrooms.mddapi.application.post.service;

import com.openclassrooms.mddapi.application.post.service.GetPostService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.infrastructure.post.mapper.PostMapper;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;

class GetPostServiceTest {

    private PostMapper postMapper;
    private PostDataPort postDataPort;
    private GetPostService service;

    @BeforeEach
    void setUp() {
        postMapper = mock(PostMapper.class);
        postDataPort = mock(PostDataPort.class);
        service = new GetPostService(postMapper, postDataPort);
    }

    @Test
    void execute_shouldReturnPostDto() {
        Post post = new Post("Integration Test Post", "Post content", null, null);

        when(postDataPort.getById(1L)).thenReturn(post);

        PostDto dto = new PostDto(
            1L,
            "Integration Test Post",
            "Post content",
            null,
            null,
            null,
            null,
            null,
            null
        );

        when(postMapper.toDto(post)).thenReturn(dto);

        PostDto result = service.execute(1L);

        verify(postDataPort).getById(1L);

        verify(postMapper).toDto(post);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Integration Test Post");
        assertThat(result.content()).isEqualTo("Post content");
    }
}
