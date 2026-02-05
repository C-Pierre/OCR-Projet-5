package com.openclassrooms.mddapi.application.post.service;

import java.util.List;

import com.openclassrooms.mddapi.application.post.service.GetPostsForUserFeed;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.infrastructure.post.mapper.PostMapper;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;

class GetPostsForUserFeedServiceTest {

    private PostMapper postMapper;
    private PostDataPort postDataPort;
    private GetPostsForUserFeed service;

    @BeforeEach
    void setUp() {
        postMapper = mock(PostMapper.class);
        postDataPort = mock(PostDataPort.class);
        service = new GetPostsForUserFeed(postMapper, postDataPort);
    }

    @Test
    void execute_shouldReturnListOfPostDto() {
        Post post1 = new Post("Title 1", "Content 1", null, null);
        Post post2 = new Post("Title 2", "Content 2", null, null);

        when(postDataPort.findAllByUserSubscriptions(42L)).thenReturn(List.of(post1, post2));

        PostDto dto1 = new PostDto(
            1L,
            "Title 1",
            "Content 1",
            null,
            null,
            null,
            null,
            null,
            null
        );
        PostDto dto2 = new PostDto(
            2L,
            "Title 2",
            "Content 2",
            null,
            null,
            null,
            null,
            null,
            null
        );

        when(postMapper.toDto(post1)).thenReturn(dto1);
        when(postMapper.toDto(post2)).thenReturn(dto2);

        List<PostDto> result = service.execute(42L);

        verify(postDataPort).findAllByUserSubscriptions(42L);
        verify(postMapper).toDto(post1);
        verify(postMapper).toDto(post2);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);
    }
}
