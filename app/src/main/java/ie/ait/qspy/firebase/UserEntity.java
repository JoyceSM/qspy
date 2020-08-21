package ie.ait.qspy.firebase;

import java.util.Date;
import java.util.List;

public class UserEntity {

    private Date date;
    private int points;
    private List<QueueSubscription> queueSubscribe;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<QueueSubscription> getQueueSubscribe() {
        return queueSubscribe;
    }

    public void setQueueSubscribe(List<QueueSubscription> queueSubscribe) {
        this.queueSubscribe = queueSubscribe;
    }
}

