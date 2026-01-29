package com.openclassrooms.mddapi.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.post.dto.PostDto;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "author.userName", target = "authorUsername")
    PostDto toDto(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "author", ignore = true)
    Post toEntity(PostDto dto);
}
