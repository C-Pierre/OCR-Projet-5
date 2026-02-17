package com.openclassrooms.mddapi.application.post.service;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.api.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.infrastructure.post.mapper.PostMapper;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.subject.repository.port.SubjectDataPort;

@Service
public class CreatePostService {

    private final PostMapper postMapper;
    private final PostDataPort postDataPort;
    private final SubjectDataPort subjectDataPort;
    private final UserDataPort userDataPort;

    public CreatePostService(
        PostMapper postMapper,
        PostDataPort postDataPort,
        SubjectDataPort subjectDataPort,
        UserDataPort userDataPort
    ) {
        this.postMapper = postMapper;
        this.postDataPort = postDataPort;
        this.subjectDataPort = subjectDataPort;
        this.userDataPort = userDataPort;
    }

    @CacheEvict(value = {"postsCache"}, allEntries = true)
    public PostDto execute(CreatePostRequest request) {
        User user = userDataPort.getById(request.authorId());
        Subject subject = subjectDataPort.getById(request.subjectId());

        Post post = new Post(request.title(), request.content(), subject, user);

        postDataPort.save(post);

        return postMapper.toDto(post);
    }
}
