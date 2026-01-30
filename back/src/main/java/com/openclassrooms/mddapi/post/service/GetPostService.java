package com.openclassrooms.mddapi.post.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.mapper.PostMapper;
import com.openclassrooms.mddapi.post.repository.port.PostDataPort;

@Service
public class GetPostService {

    private final PostMapper postMapper;
    private final PostDataPort postDataPort;

    public GetPostService(
        PostMapper postMapper,
        PostDataPort postDataPort
    ) {
        this.postMapper = postMapper;
        this.postDataPort = postDataPort;
    }

    public PostDto execute(Long id) {
        return postMapper.toDto(postDataPort.getById(id));
    }
}
