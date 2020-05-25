package orbital.gns.tpspacefinder.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.Location;
import orbital.gns.tpspacefinder.Classes.LocationPackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;


public class LocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Location currentLocation;
    private ImageView locationImageView;
    private TextView locationName;
    private Button favouritesButton;
    private MapView mapView;

    private GoogleMap gmap;

    private FirebasePackage firebase;
    private LocationPackage locationPackage;

    private LatLng currentLatLng = new LatLng(0,0);
    private User myUser;
    private int templateUsed;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    String CHANNEL_ID = "1001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        createNotificationChannel();
        firebase = new FirebasePackage();
        locationPackage = new LocationPackage();

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        currentLocation = (Location) bundle.getSerializable("CurrentLocationDetails");
        setContentViewBasedOnLayout();
        setItemsToIds();
        generateLocationDetails(savedInstanceState, bundle);

        firebase.userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myUser = documentSnapshot.toObject(User.class);
                if (myUser.favouriteLocations.contains(currentLocation.getName())) {
                    favouritesButton.setBackgroundResource(R.drawable.filledheart);
                }
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibratePhone();
                if (Objects.equals(favouritesButton.getBackground().getConstantState(), getResources().getDrawable(R.drawable.emptyheart).getConstantState())) {
                    favouritesButton.setBackgroundResource(R.drawable.filledheart);
                    pushNotificationToDevice("You have added " + currentLocation.getName() + " to your list of favourite locations.");
                    myUser.favouriteLocations.add(currentLocation.getName());
                } else if (Objects.equals(favouritesButton.getBackground().getConstantState(), getResources().getDrawable(R.drawable.filledheart).getConstantState())) {
                    favouritesButton.setBackgroundResource(R.drawable.emptyheart);
                    pushNotificationToDevice("You have removed " + currentLocation.getName() + " to your list of favourite locations.");
                    myUser.favouriteLocations.remove(currentLocation.getName());
                } else {
                    Log.d("debug", "Error");
                }

                firebase.userReference.set(myUser);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDetailsActivity.super.onBackPressed();
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Spc";
            String description = "wot";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void pushNotificationToDevice(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(100, builder.build());
    }

    /**
     * Vibrate phone
     */
    private void vibratePhone() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                  Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

    /**
     * Checks the number of seats in the chosen location, then allocate a view accordingly.
     */
    private void setContentViewBasedOnLayout() {
        templateUsed = currentLocation.seats.size();
        if (templateUsed == 40) {
            setContentView(R.layout.activity_location_details40);
        } else if (templateUsed == 50){
            setContentView(R.layout.activity_location_details50);
        } else {
            setContentView(R.layout.activity_location_details60);
        }
    }

    /**
     * This function generates the information to be displayed on the screen. It works by retrieving the location from the action page.
     * Then it retrieves the coordinates to be used in the map from the LocationPackage class. It sets the imageView to display the correct
     * location image.
     * @param savedInstanceState
     * @param bundle
     */
    private void generateLocationDetails(Bundle savedInstanceState, Bundle bundle) {
        currentLatLng = locationPackage.allLocationsLatLng.get(currentLocation.getName());
        locationImageView.setImageResource(locationPackage.allLocationsDrawable.get(currentLocation.getName()));
        locationName.setText(currentLocation.getName());
        Bundle mapViewBundle = null;

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    /**
     * Sets the objects to their respective Ids.
     */
    private void setItemsToIds() {
        locationImageView = findViewById(R.id.locationImageView);
        locationName = findViewById(R.id.locationName);
        mapView = findViewById(R.id.map);
        favouritesButton = findViewById(R.id.favouritesButton);
        if (templateUsed == 40) {
            setButtonState(R.id.a0,"A0");
            setButtonState(R.id.a1,"A1");
            setButtonState(R.id.a2,"A2");
            setButtonState(R.id.a3,"A3");
            setButtonState(R.id.a4,"A4");
            setButtonState(R.id.b0,"B0");
            setButtonState(R.id.b1,"B1");
            setButtonState(R.id.b2,"B2");
            setButtonState(R.id.b3,"B3");
            setButtonState(R.id.b4,"B4");
            setButtonState(R.id.c0,"C0");
            setButtonState(R.id.c1,"C1");
            setButtonState(R.id.c2,"C2");
            setButtonState(R.id.c3,"C3");
            setButtonState(R.id.c4,"C4");
            setButtonState(R.id.d0,"D0");
            setButtonState(R.id.d1,"D1");
            setButtonState(R.id.d2,"D2");
            setButtonState(R.id.d3,"D3");
            setButtonState(R.id.d4,"D4");
            setButtonState(R.id.e0,"E0");
            setButtonState(R.id.e1,"E1");
            setButtonState(R.id.e2,"E2");
            setButtonState(R.id.e3,"E3");
            setButtonState(R.id.e4,"E4");
            setButtonState(R.id.f0,"F0");
            setButtonState(R.id.f1,"F1");
            setButtonState(R.id.f2,"F2");
            setButtonState(R.id.f3,"F3");
            setButtonState(R.id.f4,"F4");
            setButtonState(R.id.g0,"G0");
            setButtonState(R.id.g1,"G1");
            setButtonState(R.id.g2,"G2");
            setButtonState(R.id.g3,"G3");
            setButtonState(R.id.g4,"G4");
            setButtonState(R.id.h0,"H0");
            setButtonState(R.id.h1,"H1");
            setButtonState(R.id.h2,"H2");
            setButtonState(R.id.h3,"H3");
            setButtonState(R.id.h4,"H4");

        } else if (templateUsed == 50) {
            setButtonState(R.id.a0,"A0");
            setButtonState(R.id.a1,"A1");
            setButtonState(R.id.a2,"A2");
            setButtonState(R.id.a3,"A3");
            setButtonState(R.id.a4,"A4");
            setButtonState(R.id.a5,"A5");
            setButtonState(R.id.a6,"A6");
            setButtonState(R.id.a7,"A7");
            setButtonState(R.id.a8,"A8");
            setButtonState(R.id.a9,"A9");
            setButtonState(R.id.b0,"B0");
            setButtonState(R.id.b1,"B1");
            setButtonState(R.id.b2,"B2");
            setButtonState(R.id.b3,"B3");
            setButtonState(R.id.b4,"B4");
            setButtonState(R.id.b5,"B5");
            setButtonState(R.id.b6,"B6");
            setButtonState(R.id.b7,"B7");
            setButtonState(R.id.b8,"B8");
            setButtonState(R.id.b9,"B9");
            setButtonState(R.id.c0,"C0");
            setButtonState(R.id.c1,"C1");
            setButtonState(R.id.c2,"C2");
            setButtonState(R.id.c3,"C3");
            setButtonState(R.id.c4,"C4");
            setButtonState(R.id.c5,"C5");
            setButtonState(R.id.c6,"C6");
            setButtonState(R.id.c7,"C7");
            setButtonState(R.id.c8,"C8");
            setButtonState(R.id.c9,"C9");
            setButtonState(R.id.d0,"D0");
            setButtonState(R.id.d1,"D1");
            setButtonState(R.id.d2,"D2");
            setButtonState(R.id.d3,"D3");
            setButtonState(R.id.d4,"D4");
            setButtonState(R.id.d5,"D5");
            setButtonState(R.id.d6,"D6");
            setButtonState(R.id.d7,"D7");
            setButtonState(R.id.d8,"D8");
            setButtonState(R.id.d9,"D9");
            setButtonState(R.id.e0,"E0");
            setButtonState(R.id.e1,"E1");
            setButtonState(R.id.e2,"E2");
            setButtonState(R.id.e3,"E3");
            setButtonState(R.id.e4,"E4");
            setButtonState(R.id.e5,"E5");
            setButtonState(R.id.e6,"E6");
            setButtonState(R.id.e7,"E7");
            setButtonState(R.id.e8,"E8");
            setButtonState(R.id.e9,"E9");
        } else {
            setButtonState(R.id.a0,"A0");
            setButtonState(R.id.a1,"A1");
            setButtonState(R.id.a2,"A2");
            setButtonState(R.id.a3,"A3");
            setButtonState(R.id.a4,"A4");
            setButtonState(R.id.a5,"A5");
            setButtonState(R.id.a6,"A6");
            setButtonState(R.id.a7,"A7");
            setButtonState(R.id.a8,"A8");
            setButtonState(R.id.a9,"A9");
            setButtonState(R.id.b0,"B0");
            setButtonState(R.id.b1,"B1");
            setButtonState(R.id.b2,"B2");
            setButtonState(R.id.b3,"B3");
            setButtonState(R.id.b4,"B4");
            setButtonState(R.id.b5,"B5");
            setButtonState(R.id.b6,"B6");
            setButtonState(R.id.b7,"B7");
            setButtonState(R.id.b8,"B8");
            setButtonState(R.id.b9,"B9");
            setButtonState(R.id.c0,"C0");
            setButtonState(R.id.c1,"C1");
            setButtonState(R.id.c2,"C2");
            setButtonState(R.id.c3,"C3");
            setButtonState(R.id.c4,"C4");
            setButtonState(R.id.c5,"C5");
            setButtonState(R.id.c6,"C6");
            setButtonState(R.id.c7,"C7");
            setButtonState(R.id.c8,"C8");
            setButtonState(R.id.c9,"C9");
            setButtonState(R.id.d0,"D0");
            setButtonState(R.id.d1,"D1");
            setButtonState(R.id.d2,"D2");
            setButtonState(R.id.d3,"D3");
            setButtonState(R.id.d4,"D4");
            setButtonState(R.id.d5,"D5");
            setButtonState(R.id.d6,"D6");
            setButtonState(R.id.d7,"D7");
            setButtonState(R.id.d8,"D8");
            setButtonState(R.id.d9,"D9");
            setButtonState(R.id.e0,"E0");
            setButtonState(R.id.e1,"E1");
            setButtonState(R.id.e2,"E2");
            setButtonState(R.id.e3,"E3");
            setButtonState(R.id.e4,"E4");
            setButtonState(R.id.e5,"E5");
            setButtonState(R.id.e6,"E6");
            setButtonState(R.id.e7,"E7");
            setButtonState(R.id.e8,"E8");
            setButtonState(R.id.e9,"E9");
            setButtonState(R.id.f0,"F0");
            setButtonState(R.id.f1,"F1");
            setButtonState(R.id.f2,"F2");
            setButtonState(R.id.f3,"F3");
            setButtonState(R.id.f4,"F4");
            setButtonState(R.id.f5,"F5");
            setButtonState(R.id.f6,"F6");
            setButtonState(R.id.f7,"F7");
            setButtonState(R.id.f8,"F8");
            setButtonState(R.id.f9,"F9");
        }
    }

    private void setButtonState(int p, String seat) {
        ToggleButton a1 = findViewById(p);
        Log.d("debug", seat);
        if (currentLocation.seats.get(seat)) {
            a1.setBackgroundDrawable(getResources().getDrawable(R.drawable.redseat));
        } else {
            a1.setBackgroundDrawable(getResources().getDrawable(R.drawable.blackseat));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(17);
        googleMap.addMarker(new MarkerOptions().position(currentLatLng)
                .title(currentLocation.getName()));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
    }


}
