package com.openclassrooms.mddapi.infrastructure.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.application.post.dto.PostDto;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "author.userName", target = "authorUsername")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "author.id", target = "authorId")
    PostDto toDto(Post post);
}
