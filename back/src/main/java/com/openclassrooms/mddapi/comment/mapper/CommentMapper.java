package com.openclassrooms.mddapi.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.openclassrooms.mddapi.comment.entity.Comment;
import com.openclassrooms.mddapi.comment.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.userName", target = "authorUsername")
    CommentDto toDto(Comment comment);
}
