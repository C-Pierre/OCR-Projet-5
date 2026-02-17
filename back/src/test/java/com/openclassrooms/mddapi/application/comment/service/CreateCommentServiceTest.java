package com.openclassrooms.mddapi.application.comment.service;

import java.time.LocalDateTime;

import com.openclassrooms.mddapi.application.comment.service.CreateCommentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.application.comment.dto.CommentDto;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.infrastructure.comment.mapper.CommentMapper;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.api.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.infrastructure.comment.repository.port.CommentDataPort;

class CreateCommentServiceTest {

    private CommentMapper commentMapper;
    private PostDataPort postDataPort;
    private CommentDataPort commentDataPort;
    private UserDataPort userDataPort;
    private CreateCommentService service;

    @BeforeEach
    void setUp() {
        commentMapper = mock(CommentMapper.class);
        postDataPort = mock(PostDataPort.class);
        commentDataPort = mock(CommentDataPort.class);
        userDataPort = mock(UserDataPort.class);
        service = new CreateCommentService(commentMapper, postDataPort, commentDataPort, userDataPort);
    }

    @Test
    void execute_shouldCreateCommentAndReturnDto() {
        Post post = new Post("Title", "Content", null, null);
        User user = new User("author@test.com", "author");

        when(postDataPort.getById(1L)).thenReturn(post);
        when(userDataPort.getById(2L)).thenReturn(user);

        CreateCommentRequest request = new CreateCommentRequest("My comment", 1L, 2L);

        CommentDto dto = new CommentDto(
            1L,
            "My comment",
            1L,
            2L,
            "author",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        when(commentMapper.toDto(any(Comment.class))).thenReturn(dto);

        CommentDto result = service.execute(request);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDataPort).save(captor.capture());
        Comment savedComment = captor.getValue();

        assertThat(savedComment.getContent()).isEqualTo("My comment");
        assertThat(savedComment.getPost()).isEqualTo(post);
        assertThat(savedComment.getAuthor()).isEqualTo(user);

        verify(commentMapper).toDto(savedComment);

        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("My comment");
        assertThat(result.postId()).isEqualTo(1L);
        assertThat(result.authorId()).isEqualTo(2L);
        assertThat(result.authorUsername()).isEqualTo("author");
    }
}
