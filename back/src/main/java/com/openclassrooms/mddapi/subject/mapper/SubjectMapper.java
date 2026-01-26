package com.openclassrooms.mddapi.subject.mapper;

import java.util.List;
import java.util.Collections;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.stream.Collectors;
import com.openclassrooms.mddapi.subject.model.Subject;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.common.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public abstract class SubjectMapper implements EntityMapper<SubjectDto, Subject> {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    public abstract SubjectDto toDto(Subject subject);

    @Override
    public Subject toEntity(SubjectDto dto) {
        if (dto == null) return null;

        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());

        return subject;
    }

    @Override
    public List<Subject> toEntity(List<SubjectDto> dtoList) {
        if (dtoList == null) return Collections.emptyList();
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> toDto(List<Subject> entityList) {
        if (entityList == null) return Collections.emptyList();
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
