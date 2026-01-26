package com.openclassrooms.mddapi.subject.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.mddapi.subject.dto.SubjectDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.subject.service.GetSubjectsService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final GetSubjectsService getSubjectsService;

    public SubjectController(
        GetSubjectsService getSubjectsService
    ) {
        this.getSubjectsService = getSubjectsService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> findAll() {
        return ResponseEntity.ok(getSubjectsService.execute());
    }
}