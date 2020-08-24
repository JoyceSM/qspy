package ie.ait.qspy.firebase.entities;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class StoreEntity {

    private String name;
    private String address;
    private GeoPoint coordinates;
    private List<QueueRecordEntity> queueRecords;

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

    public List<QueueRecordEntity> getQueueRecords() {
        return queueRecords;
    }

    public void setQueueRecords(List<QueueRecordEntity> queueRecords) {
        this.queueRecords = queueRecords;
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }
}
