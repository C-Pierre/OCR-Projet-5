package com.openclassrooms.mddapi.subject.mapper;

import org.mapstruct.Mapper;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectDto toDto(Subject subject);
}
