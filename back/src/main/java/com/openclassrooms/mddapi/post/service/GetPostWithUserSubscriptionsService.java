package com.openclassrooms.mddapi.post.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.post.dto.PostDto;
import com.openclassrooms.mddapi.post.mapper.PostMapper;
import com.openclassrooms.mddapi.post.repository.port.PostDataPort;

@Service
public class GetPostWithUserSubscriptionsService {

    private final PostMapper postMapper;
    private final PostDataPort postDataPort;

    public GetPostWithUserSubscriptionsService(
        PostMapper postMapper,
        PostDataPort postDataPort
    ) {
        this.postMapper = postMapper;
        this.postDataPort = postDataPort;
    }

    /**
     * Récupère tous les posts pour les sujets auxquels l'utilisateur est abonné.
     * @param userId L'ID de l'utilisateur
     * @return Liste de PostDto
     */
    public List<PostDto> execute(Long userId) {
        return postDataPort.findAllByUserSubscriptions(userId)
            .stream()
            .map(postMapper::toDto)
            .toList();
    }
}
