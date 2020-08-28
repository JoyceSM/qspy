package ie.ait.qspy.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UserEntityTest {

    private UserEntity user;

    @Before
    public void setUp() {
        user = new UserEntity();

    }

    @Test
    public void getDate() {
        Date date = new Date();
        user.setDate(date);
        assertEquals(date, user.getDate());
    }


    @Test
    public void getPoints() {
        user.setPoints(10);
        assertEquals(10, user.getPoints());
    }

    @Test
    public void getQueueSubscribe() {
        QueueSubscriptionEntity queueSubscriptionEntity = new QueueSubscriptionEntity();
        queueSubscriptionEntity.setStoreId("CHYukOYFSTRGU");
        Date date = new Date();
        queueSubscriptionEntity.setDate(date);
        user.getQueueSubscriptions().add(queueSubscriptionEntity);
        assertEquals("CHYukOYFSTRGU", queueSubscriptionEntity.getStoreId());
        assertEquals(date, queueSubscriptionEntity.getDate());
        assertFalse(user.getQueueSubscriptions().isEmpty());

    }

}