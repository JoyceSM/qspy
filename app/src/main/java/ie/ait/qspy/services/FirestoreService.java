package ie.ait.qspy.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ie.ait.qspy.R;
import ie.ait.qspy.utils.DeviceUtils;

public class FirestoreService extends Service {

    private long currentPoints = 0;
    private boolean firstAccess = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String deviceId = new DeviceUtils().getDeviceId(getContentResolver());
        FirebaseFirestore.getInstance().collection("users")
                .document(deviceId) //Create a listener for changes so the list of subscriptions is updated once it changes.
                .addSnapshotListener((userDocument, e) -> {
                    long actualPoints = (long) userDocument.get("points");
                    if (actualPoints > currentPoints && !firstAccess) { //Check if the changes are on the subscribed store (not points!).
                        currentPoints = actualPoints;
                        return;
                    }
                    firstAccess = false;
                    List<HashMap<String, Object>> subscriptionList = (List<HashMap<String, Object>>) userDocument.get("queueSubscription");
                    if (subscriptionList == null || subscriptionList.isEmpty()) {
                        return;
                    }
                    for (HashMap<String, Object> subscription : subscriptionList) {

                        FirebaseFirestore.getInstance().collection("stores")
                                .document((String) subscription.get("storeId"))
                                .addSnapshotListener((storeDocument, e1) -> { //for each subscribed store, a new listener is added (the user get notified when there are new queue reports)
                                    if (storeDocument.getMetadata().isFromCache()) { //this skips the listener for the first time (this will allow us to listen only to new changes, not the current ones)
                                        return;
                                    }
                                    String storeName = (String) storeDocument.get("name");
                                    List<HashMap<String, Object>> queueRecords = (List<HashMap<String, Object>>) storeDocument.get("queueRecords");
                                    if (queueRecords != null) {
                                        HashMap<String, Object> queueRecord = queueRecords.get(queueRecords.size() - 1);
                                        sendNotification(storeName, (Long) queueRecord.get("length"), (Timestamp) queueRecord.get("date"));
                                    }
                                });
                    }
                });
    }
    //Create send notification.
    private void sendNotification(String storeName, Long length, Timestamp date) {
        String formattedDate = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(date.toDate());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getString(R.string.default_channel_id);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Queue length change for store " + storeName)
                .setContentText("New report at " + formattedDate + ": " + length + " people in the queue")
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}
