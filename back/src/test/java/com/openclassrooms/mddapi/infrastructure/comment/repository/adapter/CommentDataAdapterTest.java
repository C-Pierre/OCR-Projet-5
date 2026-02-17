package com.openclassrooms.mddapi.infrastructure.comment.repository.adapter;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import com.openclassrooms.mddapi.domain.comment.repository.CommentRepository;

class CommentDataAdapterTest {

    private CommentRepository commentRepository;
    private CommentDataAdapter commentDataAdapter;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentDataAdapter = new CommentDataAdapter(commentRepository);
    }

    @Test
    void findPostId_shouldDelegateToRepository() {
        Long postId = 1L;
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findByPostId(postId)).thenReturn(comments);

        List<Comment> result = commentDataAdapter.findPostId(postId);

        assertThat(result).isSameAs(comments);
        verify(commentRepository, times(1)).findByPostId(postId);
    }

    @Test
    void save_shouldDelegateToRepository() {
        Comment comment = mock(Comment.class);

        commentDataAdapter.save(comment);

        verify(commentRepository, times(1)).save(comment);
    }
}