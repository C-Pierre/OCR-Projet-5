package com.openclassrooms.mddapi.subject.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.subject.service.GetSubjectService;
import com.openclassrooms.mddapi.subject.service.GetSubjectsService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final GetSubjectService getSubjectService;
    private final GetSubjectsService getSubjectsService;

    public SubjectController(
        GetSubjectService getSubjectService,
        GetSubjectsService getSubjectsService
    ) {
        this.getSubjectService = getSubjectService;
        this.getSubjectsService = getSubjectsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getSubjectService.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> findAll() {
        return ResponseEntity.ok(getSubjectsService.execute());
    }
}