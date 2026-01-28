package com.openclassrooms.mddapi.subscription.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.openclassrooms.mddapi.user.model.User;
import org.springframework.data.annotation.CreatedDate;
import com.openclassrooms.mddapi.subject.model.Subject;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "subscription", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "subject_id"}))
@EntityListeners(AuditingEntityListener.class)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @CreatedDate
    @Column(updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    protected Subscription() {}

    public Subscription(User user, Subject subject) {
        this.user = user;
        this.subject = subject;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Subject getSubject() { return subject; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setUser(User user) { this.user = user; }
    public void setSubject(Subject subject) { this.subject = subject; }
}
