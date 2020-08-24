package ie.ait.qspy.firebase;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import ie.ait.qspy.firebase.entities.QueueRecordEntity;

import static org.junit.Assert.*;

public class QueueRecordEntityTest {

    private QueueRecordEntity queueRecord;

    @Before
    public void setUp() throws Exception {
        queueRecord = new QueueRecordEntity();

    }

    @Test
    public void getDate() {
        Date date = new Date();
        queueRecord.setDate(date);
        assertEquals(date, queueRecord.getDate());
    }


    @Test
    public void getUserId() {
        queueRecord.setUserId("HAJEUALDMQOENJA");
        assertEquals("HAJEUALDMQOENJA", queueRecord.getUserId());
    }

    @Test
    public void getLength() {
        queueRecord.setLength(8);
        assertEquals(8, queueRecord.getLength());
    }

}