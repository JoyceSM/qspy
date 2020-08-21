package ie.ait.qspy.entity;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class StoreDetailsTest {

    private StoreDetails storeDetails;

    @Before
    public void setUp() throws Exception {
        LatLng latLng = new LatLng(53.423579, -7.938644);
        storeDetails = new StoreDetails("Marks & Spencer", "Town Centre, 51 Gleeson St, Athlone, Co. Westmeath", latLng, place.getId(), Arrays.asList("Marks & Spencer", "Town Centre, 51 Gleeson St, Athlone, Co. Westmeath"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        assertEquals("Marks & Spencer", storeDetails.getName());
    }

    @Test
    public void getAddress() {
        assertEquals("Town Centre, 51 Gleeson St, Athlone, Co. Westmeath", storeDetails.getAddress());
    }

    @Test
    public void getCoordinates() {
        assertEquals(53.423579, storeDetails.getCoordinates().latitude, 0.0);
        assertEquals(-7.938644, storeDetails.getCoordinates().longitude, 0.0);
    }

    @Test
    public void getAttributions() {
        assertTrue(storeDetails.getAttributions().contains("Marks & Spencer"));
        assertTrue(storeDetails.getAttributions().contains("Town Centre, 51 Gleeson St, Athlone, Co. Westmeath"));
    }


}