package ie.ait.qspy.services;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ie.ait.qspy.firebase.entities.LevelEntity;

public class LevelService extends AbstractService {

    private List<LevelEntity> levels = new ArrayList<>();

    public LevelService() {
        //create level
        getDatabase().collection("level").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot doc : documents) {
                LevelEntity level = new LevelEntity(doc.getString("description"), doc.getLong("rangeStart"), doc.getLong("rangeEnd"), doc.getString("avatarImg"));
                levels.add(level);
            }
        });
    }

    public LevelEntity getUserLevel(Long points) {

        if (levels.isEmpty()) {
//            throw new RuntimeException("Levels is not populated yet.");
            return null;
        }

        for (LevelEntity level : levels) {
            if (points >= level.getRangeStart() && points <= level.getRangeEnd()) {

                return level;

            }
        }
        return null;
    }


}
