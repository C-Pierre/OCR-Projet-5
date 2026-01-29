package com.openclassrooms.mddapi.post.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.common.exception.NotFoundException;

public interface PostDataPort {
    Post getById(Long id) throws NotFoundException;
    List<Post> findAll();
    List<Post> findAllByUserSubscriptions(Long subjectId);
    void save(Post post);
}