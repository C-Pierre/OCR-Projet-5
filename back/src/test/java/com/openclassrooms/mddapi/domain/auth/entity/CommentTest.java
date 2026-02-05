package com.openclassrooms.mddapi.domain.auth.entity;

import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;

class CommentTest {

    private Subject subject;
    private Comment comment;
    private Post post;
    private User author;

    @BeforeEach
    void setUp() {
        subject = new Subject("Subject", "Subject");
        author = new User("author@test.com", "author");
        post = new Post("Title", "Content", subject, author);
        comment = new Comment("My comment", post, author);
    }

    @Test
    void constructorAndGetters_shouldWork() {
        assertEquals("My comment", comment.getContent());
        assertEquals(post, comment.getPost());
        assertEquals(author, comment.getAuthor());
        assertNull(comment.getId());
        assertNull(comment.getCreatedAt());
        assertNull(comment.getUpdatedAt());
    }

    @Test
    void setters_shouldModifyFields() {
        User newAuthor = new User("new@test.com", "newAuthor");
        Post newPost = new Post("Other Title", "Other Content", subject, newAuthor);

        comment.setContent("Updated comment");
        comment.setPost(newPost);
        comment.setAuthor(newAuthor);

        assertEquals("Updated comment", comment.getContent());
        assertEquals(newPost, comment.getPost());
        assertEquals(newAuthor, comment.getAuthor());
    }
}
