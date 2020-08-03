package ie.ait.qspy.entity;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class StoreDetails {

    private String name;
    private String address;
    private LatLng coordinates;
    private List<String> attributions;


    public StoreDetails(String name, String address, LatLng coordinates, List<String> attributions) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.attributions = attributions;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public List<String> getAttributions() {
        return attributions;
    }
}
