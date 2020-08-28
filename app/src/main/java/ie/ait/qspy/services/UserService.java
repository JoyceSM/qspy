package ie.ait.qspy.services;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import ie.ait.qspy.entities.UserEntity;

public class UserService extends AbstractService {

    private static final String COLLECTION_NAME = "users";

    public void listenForChanges(String docId, EventListener<DocumentSnapshot> listener) {
        getDatabase().collection(COLLECTION_NAME).document(docId).addSnapshotListener(listener);
    }

    public void save(String docId, UserEntity user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        getDatabase().collection(COLLECTION_NAME).document(docId)
                .set(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updateField(String docId, String field, Object value) {
        getDatabase().collection(COLLECTION_NAME).document(docId).update(field, value);
    }

}
