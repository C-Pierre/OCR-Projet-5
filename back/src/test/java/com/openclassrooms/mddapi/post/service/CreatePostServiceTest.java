package com.openclassrooms.mddapi.post.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.post.mapper.PostMapper;
import com.openclassrooms.mddapi.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;

class CreatePostServiceTest {

    private PostMapper postMapper;
    private PostDataPort postDataPort;
    private SubjectDataPort subjectDataPort;
    private UserDataPort userDataPort;
    private CreatePostService service;

    @BeforeEach
    void setUp() {
        postMapper = mock(PostMapper.class);
        postDataPort = mock(PostDataPort.class);
        subjectDataPort = mock(SubjectDataPort.class);
        userDataPort = mock(UserDataPort.class);

        service = new CreatePostService(postMapper, postDataPort, subjectDataPort, userDataPort);
    }

    @Test
    void execute_shouldCreatePostAndReturnDto() {
        User author = new User("author@test.com", "author");
        Subject subject = new Subject("Math", "Math description");

        when(userDataPort.getById(2L)).thenReturn(author);
        when(subjectDataPort.getById(1L)).thenReturn(subject);

        CreatePostRequest request = new CreatePostRequest(
            "Integration Test Post",
            "This is post content",
            1L,
            2L
        );

        PostDto dto = new PostDto(
            1L,
            "Integration Test Post",
            "This is post content",
            1L,
            "Math",
            2L,
            "author",
            null,
            null
        );
        when(postMapper.toDto(any(Post.class))).thenReturn(dto);

        PostDto result = service.execute(request);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postDataPort).save(captor.capture());
        Post savedPost = captor.getValue();

        assertThat(savedPost.getTitle()).isEqualTo("Integration Test Post");
        assertThat(savedPost.getContent()).isEqualTo("This is post content");
        assertThat(savedPost.getAuthor()).isEqualTo(author);
        assertThat(savedPost.getSubject()).isEqualTo(subject);

        verify(postMapper).toDto(savedPost);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Integration Test Post");
        assertThat(result.content()).isEqualTo("This is post content");
        assertThat(result.subjectId()).isEqualTo(1L);
        assertThat(result.subjectName()).isEqualTo("Math");
        assertThat(result.authorId()).isEqualTo(2L);
        assertThat(result.authorUsername()).isEqualTo("author");
    }
}
