package ie.ait.qspy.firebase;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import ie.ait.qspy.firebase.entities.UserEntity;

import static org.junit.Assert.*;

public class UserEntityTest {

    private UserEntity user;

    @Before
    public void setUp() throws Exception {
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

    }

    @Test
    public void setQueueSubscribe() {

    }
}