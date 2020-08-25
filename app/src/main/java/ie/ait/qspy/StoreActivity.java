package ie.ait.qspy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ie.ait.qspy.services.FirestoreService;
import ie.ait.qspy.services.StoreService;

public class StoreActivity extends AppCompatActivity {

    Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        getStoreById();

        btnSignOut = findViewById(R.id.btn_signOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signOutIntent = new Intent(StoreActivity.this, MapsActivity.class);
                startActivity(signOutIntent);
            }
        });
    }

    private void getStoreById() {
        Intent storeIntent = getIntent();
        String storeId = storeIntent.getStringExtra("storeId");
        StoreService storeService = new StoreService();
        storeService.getById(storeId, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = (String) documentSnapshot.get("name");
                TextView storeName = findViewById(R.id.store_name);
                storeName.setText(name);
            }
        });
    }
}