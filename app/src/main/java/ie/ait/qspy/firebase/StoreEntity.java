package ie.ait.qspy.firebase;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class StoreEntity {

    private String name;
    private String address;
    private String phone;
    private String secretKey;
    private String email;
    private String logo;
    private GeoPoint coordinates;
    private List<QueueRecord> queueRecords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<QueueRecord> getQueueRecords() {
        return queueRecords;
    }

    public void setQueueRecords(List<QueueRecord> queueRecords) {
        this.queueRecords = queueRecords;
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }
}
