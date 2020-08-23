package ie.ait.qspy.services;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class AbstractService {

    public FirebaseFirestore getDatabase() {
        return FirebaseFirestore.getInstance();
    }

}
