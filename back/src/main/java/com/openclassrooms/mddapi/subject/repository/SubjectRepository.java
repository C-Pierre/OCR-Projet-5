package com.openclassrooms.mddapi.subject.repository;

import org.springframework.stereotype.Repository;
import com.openclassrooms.mddapi.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject, Long> {}