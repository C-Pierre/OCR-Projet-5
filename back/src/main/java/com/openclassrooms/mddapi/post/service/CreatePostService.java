package com.openclassrooms.mddapi.post.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.post.mapper.PostMapper;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.post.request.CreatePostRequest;
import com.openclassrooms.mddapi.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;

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

    public PostDto execute(CreatePostRequest request) {
        User user = userDataPort.getById(request.authorId());
        Subject subject = subjectDataPort.getById(request.subjectId());

        Post post = new Post(request.title(), request.content(), subject, user);

        postDataPort.save(post);

        return postMapper.toDto(post);
    }
}
