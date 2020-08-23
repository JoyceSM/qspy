package ie.ait.qspy.firebase;

import java.util.Date;

public class QueueRecordEntity {

    private Date date;
    private String userId;
    private int length;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

