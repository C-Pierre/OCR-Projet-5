package com.openclassrooms.mddapi.user.repository;

import java.util.Optional;
import com.openclassrooms.mddapi.user.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
}