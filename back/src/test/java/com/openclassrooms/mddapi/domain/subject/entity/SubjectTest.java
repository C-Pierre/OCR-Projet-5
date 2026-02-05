package com.openclassrooms.mddapi.domain.subject.entity;

import java.util.List;

import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;

class SubjectTest {

    @Test
    void testConstructorAndGetters() {
        Subject subject = new Subject("Math", "Math description");

        assertNull(subject.getId(), "Id should be null before persisting");
        assertEquals("Math", subject.getName());
        assertEquals("Math description", subject.getDescription());
        assertNotNull(subject.getSubscriptions());
        assertTrue(subject.getSubscriptions().isEmpty());
        assertNull(subject.getCreatedAt());
        assertNull(subject.getUpdatedAt());
    }

    @Test
    void testSetters() {
        Subject subject = new Subject("Initial", "Initial desc");

        subject.setName("Physics");
        subject.setDescription("Physics desc");

        assertEquals("Physics", subject.getName());
        assertEquals("Physics desc", subject.getDescription());

        Subscription subscription1 = Mockito.mock(Subscription.class);
        Subscription subscription2 = Mockito.mock(Subscription.class);

        subject.setSubscriptions(List.of(subscription1, subscription2));
        assertEquals(2, subject.getSubscriptions().size());
    }

    @Test
    void testAddSubscription() {
        Subject subject = new Subject("Math", "Math description");

        Subscription subscription = Mockito.mock(Subscription.class);
        subject.addSubscription(subscription);

        assertEquals(1, subject.getSubscriptions().size());
        assertTrue(subject.getSubscriptions().contains(subscription));

        Mockito.verify(subscription).setSubject(subject);
    }

    @Test
    void testRemoveSubscription() {
        Subject subject = new Subject("Math", "Math description");

        Subscription subscription = Mockito.mock(Subscription.class);
        subject.addSubscription(subscription);

        assertEquals(1, subject.getSubscriptions().size());

        subject.removeSubscription(subscription);

        assertTrue(subject.getSubscriptions().isEmpty());

        Mockito.verify(subscription).setSubject(null);
    }
}
