package com.openclassrooms.mddapi.application.post.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.openclassrooms.mddapi.infrastructure.post.mapper.PostMapper;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;

@Service
public class GetPostsForUserFeed {

    private final PostMapper postMapper;
    private final PostDataPort postDataPort;

    public GetPostsForUserFeed(
        PostMapper postMapper,
        PostDataPort postDataPort
    ) {
        this.postMapper = postMapper;
        this.postDataPort = postDataPort;
    }

    @Cacheable("postsForUserFeedCache")
    public List<PostDto> execute(Long userId) {
        return postDataPort.findAllByUserSubscriptions(userId)
            .stream()
            .map(postMapper::toDto)
            .toList();
    }
}
