package ie.ait.qspy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.InputType;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ie.ait.qspy.services.StoreService;

public class StoreActivity extends AppCompatActivity {

    private Button btnSignOut;
    private Button btnOffers;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        getStoreById();
        // Access signOut button.
        btnSignOut = findViewById(R.id.btn_signOut);
        btnSignOut.setOnClickListener(view -> {
            Intent signOutIntent = new Intent(StoreActivity.this, MapsActivity.class);
            startActivity(signOutIntent);
        });
        // Create offers button.
        btnOffers = findViewById(R.id.btn_offers);
        btnOffers.setOnClickListener(view -> {
            Intent offerIntent = new Intent(StoreActivity.this, OfferActivity.class);
            offerIntent.putExtra("storeName", name);
            startActivity(offerIntent);


        });
        // Add logo in the action bar.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo_small);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c5aa0")));

    }

    // Get store name in the store access.
    private void getStoreById() {
        Intent storeIntent = getIntent();
        String storeId = storeIntent.getStringExtra("storeId");
        StoreService storeService = new StoreService();
        storeService.getById(storeId, documentSnapshot -> {
            name = (String) documentSnapshot.get("name");
            TextView storeName = findViewById(R.id.store_name);
            storeName.setText(name);
        });
    }
}