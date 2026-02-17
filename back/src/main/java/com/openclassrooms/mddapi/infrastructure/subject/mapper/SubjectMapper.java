package com.openclassrooms.mddapi.infrastructure.subject.mapper;

import org.mapstruct.Mapper;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectDto toDto(Subject subject);
}
