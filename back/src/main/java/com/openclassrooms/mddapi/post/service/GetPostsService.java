package com.openclassrooms.mddapi.post.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.mapper.PostMapper;
import com.openclassrooms.mddapi.post.repository.port.PostDataPort;

@Service
public class GetPostsService {

    private final PostMapper postMapper;
    private final PostDataPort postDataPort;

    public GetPostsService(
        PostMapper postMapper,
        PostDataPort postDataPort
    ) {
        this.postMapper = postMapper;
        this.postDataPort = postDataPort;
    }

    public List<PostDto> execute() {
        return postDataPort.findAll()
            .stream()
            .map(postMapper::toDto)
            .toList();
    }
}
