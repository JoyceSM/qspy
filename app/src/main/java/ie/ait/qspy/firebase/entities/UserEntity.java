package ie.ait.qspy.firebase.entities;

import java.util.Date;
import java.util.List;

public class UserEntity {

    private Date date;
    private int points;
    private List<QueueSubscriptionEntity> queueSubscribe;


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

    public List<QueueSubscriptionEntity> getQueueSubscribe() {
        return queueSubscribe;
    }

    public void setQueueSubscribe(List<QueueSubscriptionEntity> queueSubscribe) {
        this.queueSubscribe = queueSubscribe;
    }
}

