package com.openclassrooms.mddapi.infrastructure.post.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.post.repository.PostRepository;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

@Service
public class PostDataAdapter implements PostDataPort {

    private final PostRepository postRepository;

    public PostDataAdapter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    }

    @Override
    public List<Post> findAllByUserSubscriptions(Long subjectId) {
        return postRepository.findAllByUserSubscriptions(subjectId);
    }

    @Override
    public void save(Post post) { postRepository.save(post); }
}

