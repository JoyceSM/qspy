package ie.ait.qspy;

import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ie.ait.qspy.entity.StoreDetails;
import ie.ait.qspy.firebase.QueueRecord;

import ie.ait.qspy.firebase.StoreEntity;
import ie.ait.qspy.firebase.UserEntity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 30;

    // Collections
    public static final String COLLECTION_STORES = "stores";


    private CameraPosition cameraPosition;
    // The entry point to the Places API.
    private PlacesClient placesClient;

    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known location retrieved by the Fused Location Provider.
    private Location lastLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private GoogleMap map;
    SearchView searchView;

    // Access a Cloud Firestore instance from  MapActivity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //get device id
    @SuppressLint("HardwareIds")
    private String getDeviceId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Search place
        searchView = findViewById(R.id.sv_location);
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String searchInput = searchView.getQuery().toString();

                if (searchInput.equals("")) {
                    return false;
                }

                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(searchInput)
                        .setCountry("IE").build();
                Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);


                autocompletePredictions.addOnSuccessListener(findAutocompletePredictionsResponse -> {
                    List<AutocompletePrediction> prediction = findAutocompletePredictionsResponse.getAutocompletePredictions();
                    // TODO: Maybe show a dialog here with the options available
                    AutocompletePrediction autocompletePrediction = prediction.get(0);
                    String placeId = autocompletePrediction.getPlaceId();
                    db.collection(COLLECTION_STORES).document(placeId).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                Object queueRecords = documentSnapshot.get("queueRecords");
                                GeoPoint coordinates = documentSnapshot.getGeoPoint("coordinates");
                                String name = (String) documentSnapshot.get("name");
                                LatLng latLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Map<String, Object> fields : (List<Map<String, Object>>) queueRecords) {
                                    long length = (long) fields.get("length");
                                    Timestamp timestamp = (Timestamp) fields.get("date");
                                    stringBuilder.append(timestamp.toDate()).append(" - Queue length reported: ").append(length).append(System.lineSeparator());
                                }
                                Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(name).snippet(stringBuilder.toString()));
                                marker.showInfoWindow();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            });
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);

        //create floating button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showNearbyPlaces());
        saveUser();
    }

    //Sets up the options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    //Handles a click on the menu option to get a place.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.input:
                showNearbyPlaces();
                return true;

            case R.id.store_access:
                // User chose the "Store Access" item, show the store functionality...
                Toast.makeText(getApplicationContext(), "Store selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.points:
                // User chose the "Diamond" action, show a pop up message with total of points
                //  Toast.makeText(getApplicationContext(), "OK Pressed: " + picker.getValue(), Toast.LENGTH_LONG).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage(getString((R.string.points)));
                builder.show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    //If Google Play services is not installed on the device, the user will be prompted to install it inside the SupportMapFragment.
    // This method will only be triggered once the user has installed Google Play services and returned to the app.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastLocation);
        }
        super.onSaveInstanceState(outState);
    }

    //Manipulates the map when it's available. This callback is triggered when the map is ready to be used.
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        // Use a custom info window adapter to handle multiple lines of text in the info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, (FrameLayout) findViewById(R.id.map), false);

                TextView title = (TextView) infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = (TextView) infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        // Prompt the user for permission.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    //Gets the current location of the device, and positions the map's camera.
    private void getDeviceLocation() {
        //Get the best and most recent location of the device, which may be null in rare cases when a location is not available.
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastLocation = task.getResult();
                            if (lastLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    //Prompts the user for permission to use the device location.
    private void getLocationPermission() {
        //Request location permission, so that we can get the location of the device.
        // The result of the permission request is handled by a callback, onRequestPermissionsResult.
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //Handles the result of the request for location permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    //Prompts the user to select the current place from a list of likely places, and shows the current place on the map - provided the user has granted location permission.
    private void showNearbyPlaces() {
        if (map == null) {
            return;
        }
        if (locationPermissionGranted) {
            final Task<FindCurrentPlaceResponse> placeResult = retrieveCurrentPlaces();
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        final FindCurrentPlaceResponse likelyPlaces = task.getResult();
                        final Place place = likelyPlaces.getPlaceLikelihoods().get(0).getPlace();
                        final String storeName = place.getName();
                        Log.i("Place Id", String.valueOf(place.getId()));
                        new AlertDialog.Builder(MapsActivity.this)
                                .setTitle("Location")
                                .setMessage(getString(R.string.are_you_in_the_store_name, storeName))
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //select the current store
                                        StoreDetails store = new StoreDetails(place.getId(), place.getName(), place.getAddress(), place.getLatLng(), place.getAttributions());
                                        selectPlaceOnMap(store);
                                        showQueueInputDialog(store);
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        filterStoresAndShowPlacesDialog(likelyPlaces);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_menu_compass)
                                .show();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");
            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));
            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    //create user
    private void saveUser() {
        // Create a new user
        UserEntity user = new UserEntity();
        user.setDate(new Date());
        user.setPoints(0);
        // Add a new document with a generated ID
        db.collection("users").document(getDeviceId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Adding user", "DocumentSnapshot added with ID: " + getDeviceId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Adding user", "Error adding document", e);
                    }
                });
    }

    //create store record
    private void saveUserInput(StoreDetails storeInput, int value) {
        //create queue record object
        QueueRecord queueRecord = new QueueRecord();
        queueRecord.setDate(new Date());
        queueRecord.setLength(value);
        queueRecord.setUserId(getDeviceId());
        db.collection("users").document(getDeviceId()).update("points", FieldValue.increment(5));
        // Create a new store
        StoreEntity store = new StoreEntity();
        store.setName(storeInput.getName());
        store.setAddress(storeInput.getAddress());
        LatLng coord = storeInput.getCoordinates();
        store.setCoordinates(new GeoPoint(coord.latitude, coord.longitude));
        // store.setSecretKey("jd753");
        store.setQueueRecords(Collections.singletonList(queueRecord));
        //retrieve stores
        db.collection(COLLECTION_STORES).document(storeInput.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().exists()) {
                                Log.i("Retrieving document", "Document already created");
                                DocumentReference docRef = db.collection(COLLECTION_STORES).document(task.getResult().getId());
                                // Atomically add a new queueuRecord to the "length" array field.
                                docRef.update("queueRecords", FieldValue.arrayUnion(queueRecord));
                            } else {
                                // Add a new document with a generated ID
                                db.collection(COLLECTION_STORES)
                                        .document(storeInput.getId())
                                        .set(store)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Adding store", "DocumentSnapshot added with ID: " + storeInput.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Adding store", "Error adding document", e);
                                            }
                                        });
                            }

                        } else {
                            Log.w("Retrieve Store", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //Display a pop-up message allowing the user to contribute to the App.
    private void showQueueInputDialog(StoreDetails store) {
        NumberPicker picker = new NumberPicker(MapsActivity.this);
        picker.setMinValue(0);
        picker.setMaxValue(100);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage(getString((R.string.how_many_people_are_in_the_queue)));
        builder.setView(picker);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                saveUserInput(store, picker.getValue());
                //  Toast.makeText(getApplicationContext(), "OK Pressed: " + picker.getValue(), Toast.LENGTH_LONG).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage(getString((R.string.thank_you)));
                builder.show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Cancel Pressed", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        builder.show();
    }

    //Displays a form allowing the user to select a place from a list of likely places.
    private void filterStoresAndShowPlacesDialog(FindCurrentPlaceResponse likelyPlaces) {
        // Set the count, handling cases where less than 30 entries are returned.
        int count = Math.min(likelyPlaces.getPlaceLikelihoods().size(), M_MAX_ENTRIES);

        List<StoreDetails> storeDetailsList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PlaceLikelihood placeLikelihood = likelyPlaces.getPlaceLikelihoods().get(i);
            Place place = placeLikelihood.getPlace();
            StoreDetails store = new StoreDetails(place.getId(), place.getName(), place.getAddress(), place.getLatLng(), place.getAttributions());
            storeDetailsList.add(store);
        }
        // Show a dialog offering the user the list of likely places, and add a marker at the selected place.
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                StoreDetails store = storeDetailsList.get(which);
                selectPlaceOnMap(store);
                showQueueInputDialog(store);
            }
        };

        String[] names = storeDetailsList
                .stream() // Calls the stream API
                .map(StoreDetails::getName) // Perform a transformation using the map method
                .toArray(String[]::new); // Convert to array

        //Display a list of places
        new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(names, listener)
                .show();
    }

    @SuppressLint("MissingPermission")
    private Task<FindCurrentPlaceResponse> retrieveCurrentPlaces() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ID);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Get the likely places - that is, the businesses and other points of interest that are the best match for the device's current location.


        return placesClient.findCurrentPlace(request);
    }

    private void selectPlaceOnMap(StoreDetails store) {
        LatLng markerLatLng = store.getCoordinates();
        String markerSnippet = store.getAddress();
        if (store.getAttributions() != null) {
            markerSnippet = markerSnippet + "\n" + store.getAttributions();

        }
        // Add a marker for the selected place, with an info window showing information about that place.
        map.addMarker(new MarkerOptions()
                .title(store.getName())
                .position(markerLatLng)
                .snippet(markerSnippet));
        // Position the map's camera at the location of the marker.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, DEFAULT_ZOOM));
    }

    //Updates the map's UI settings based on whether the user has granted location permission.
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}