package com.openclassrooms.mddapi.comment.service;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import com.openclassrooms.mddapi.comment.entity.Comment;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.comment.mapper.CommentMapper;
import com.openclassrooms.mddapi.comment.repository.port.CommentDataPort;

class GetCommentsByPostServiceTest {

    private CommentMapper commentMapper;
    private CommentDataPort commentDataPort;
    private GetCommentsByPostService service;

    @BeforeEach
    void setUp() {
        commentMapper = mock(CommentMapper.class);
        commentDataPort = mock(CommentDataPort.class);
        service = new GetCommentsByPostService(commentMapper, commentDataPort);
    }

    @Test
    void execute_shouldReturnMappedComments() {
        Long postId = 1L;

        Comment comment1 = mock(Comment.class);
        Comment comment2 = mock(Comment.class);

        when(commentDataPort.findPostId(postId)).thenReturn(List.of(comment1, comment2));

        CommentDto dto1 = new CommentDto(
            1L,
            "Comment 1",
            postId,
            10L,
            "user1",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        CommentDto dto2 = new CommentDto(
            2L,
            "Comment 2",
            postId,
            11L,
            "user2",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(commentMapper.toDto(comment1)).thenReturn(dto1);
        when(commentMapper.toDto(comment2)).thenReturn(dto2);

        List<CommentDto> result = service.execute(postId);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);

        verify(commentDataPort).findPostId(postId);
        verify(commentMapper).toDto(comment1);
        verify(commentMapper).toDto(comment2);
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoComments() {
        Long postId = 2L;

        when(commentDataPort.findPostId(postId)).thenReturn(new ArrayList<>());

        List<CommentDto> result = service.execute(postId);

        assertThat(result).isEmpty();
        verify(commentDataPort).findPostId(postId);
        verifyNoInteractions(commentMapper);
    }
}
