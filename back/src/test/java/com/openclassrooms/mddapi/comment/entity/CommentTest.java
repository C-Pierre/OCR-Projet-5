package com.openclassrooms.mddapi.comment.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.entity.Subject;

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
