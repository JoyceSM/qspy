package ie.ait.qspy.firebase;

import org.junit.Before;
import org.junit.Test;

import ie.ait.qspy.firebase.entities.StoreEntity;

import static org.junit.Assert.*;

public class StoreEntityTest {

    private StoreEntity store;

    @Before
    public void setUp() throws Exception {
        store = new StoreEntity();
    }

    @Test
    public void getName() {
        store.setName("Zara");
        assertEquals("Zara", store.getName());
    }

    @Test
    public void getAddress() {
        store.setAddress("Towncenter, Athlone");
        assertEquals("Towncenter, Athlone", store.getAddress());
    }
}