package com.openclassrooms.mddapi.post.repository.adapter;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.openclassrooms.mddapi.post.repository.PostRepository;
import com.openclassrooms.mddapi.common.exception.type.NotFoundException;

class PostDataAdapterTest {

    private Subject subject;
    private User user;
    private PostRepository postRepository;
    private PostDataAdapter postDataAdapter;

    @BeforeEach
    void setUp() {
        subject = new Subject("Subject", "Subject");
        user = new User("email@mail.com", "NewUser");
        postRepository = mock(PostRepository.class);
        postDataAdapter = new PostDataAdapter(postRepository);
    }

    @Test
    void getById_shouldReturnPost_whenExists() {
        Post post = new Post("title", "description", subject, user);
        Long id = post.getId();
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        Post result = postDataAdapter.getById(id);

        assertThat(result).isSameAs(post);
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenNotExists() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> postDataAdapter.getById(999L));
        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    void findAllByUserSubscriptions_shouldDelegateToRepository() {
        Post post1 = new Post("title", "description", subject, user);
        Post post2 = new Post("title", "description", subject, user);
        Long userId = user.getId();
        List<Post> posts = List.of(post1, post2);
        when(postRepository.findAllByUserSubscriptions(userId)).thenReturn(posts);

        List<Post> result = postDataAdapter.findAllByUserSubscriptions(userId);

        assertThat(result).isSameAs(posts);
        verify(postRepository, times(1)).findAllByUserSubscriptions(userId);
    }

    @Test
    void save_shouldDelegateToRepository() {
        Post post = new Post("title", "description", subject, user);

        postDataAdapter.save(post);

        verify(postRepository, times(1)).save(post);
    }
}
