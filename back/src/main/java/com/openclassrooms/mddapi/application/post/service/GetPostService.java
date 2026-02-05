package com.openclassrooms.mddapi.application.post.service;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.openclassrooms.mddapi.infrastructure.post.mapper.PostMapper;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;

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

    @Cacheable(value = "postCache", key = "#id")
    public PostDto execute(Long id) {
        return postMapper.toDto(postDataPort.getById(id));
    }
}
