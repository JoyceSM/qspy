package ie.ait.qspy.services;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import ie.ait.qspy.entities.StoreEntity;

public class StoreService extends AbstractService {

    private static final String COLLECTION_NAME = "stores";

    public void getById(String docId, OnSuccessListener<DocumentSnapshot> listener) {
        getDatabase().collection(COLLECTION_NAME).document(docId).get().addOnSuccessListener(listener);

    }

    public void update(String docId, StoreEntity store, OnSuccessListener<Void> listener) {
        getDatabase().collection(COLLECTION_NAME).document(docId).set(store).addOnSuccessListener(listener);
    }

    public void updateField(String docId, String field, Object value) {
        getDatabase().collection(COLLECTION_NAME).document(docId).update(field, value);
    }


}
