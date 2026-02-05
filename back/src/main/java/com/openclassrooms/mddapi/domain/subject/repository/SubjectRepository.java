package com.openclassrooms.mddapi.domain.subject.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import org.springframework.transaction.annotation.Transactional;
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("""
        SELECT new com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto(
            s.id,
            s.name,
            s.description,
            CASE WHEN sub.id IS NOT NULL THEN true ELSE false END
        )
        FROM Subject s
        LEFT JOIN Subscription sub
            ON sub.subject.id = s.id
            AND sub.user.id = :userId
    """)
    List<SubjectWithSubscriptionDto> findAllWithSubscriptionForUser(
        @Param("userId") Long userId
    );
}