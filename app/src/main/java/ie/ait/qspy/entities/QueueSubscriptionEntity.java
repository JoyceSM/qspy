package ie.ait.qspy.entities;

import java.util.Date;

public class QueueSubscriptionEntity {

    private Date date;
    private String storeId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

}
