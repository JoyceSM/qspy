package ie.ait.qspy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ie.ait.qspy.services.FirestoreService;
import ie.ait.qspy.services.StoreService;

public class StoreActivity extends AppCompatActivity {

    Button btnSignOut;
    Button btnOffers;

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


        btnOffers = findViewById(R.id.btn_offers);
        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog offer = new AlertDialog.Builder(StoreActivity.this).create();
                offer.setTitle("Send Offer");
                final EditText input = new EditText(StoreActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
                offer.setView(input);

                offer.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();
                    }
                });
                offer.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You clicked on CANCEL", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                offer.show();

            }

        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo_small);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c5aa0")));

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