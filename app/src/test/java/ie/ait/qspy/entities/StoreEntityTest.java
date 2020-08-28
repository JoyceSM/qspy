package ie.ait.qspy.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreEntityTest {

    private StoreEntity store;

    @Before
    public void setUp() {
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