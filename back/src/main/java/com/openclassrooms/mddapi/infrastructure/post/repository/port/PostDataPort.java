package com.openclassrooms.mddapi.infrastructure.post.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

public interface PostDataPort {
    Post getById(Long id) throws NotFoundException;
    List<Post> findAllByUserSubscriptions(Long subjectId);
    void save(Post post);
}