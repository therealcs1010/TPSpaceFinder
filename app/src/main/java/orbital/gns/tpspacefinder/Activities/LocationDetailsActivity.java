package orbital.gns.tpspacefinder.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new FirebasePackage();
        locationPackage = new LocationPackage();

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        currentLocation = (Location) bundle.getSerializable("CurrentLocationDetails");

        setContentView(R.layout.activity_location_details);
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
            public void onClick(View v) {
                if (Objects.equals(favouritesButton.getBackground().getConstantState(), getResources().getDrawable(R.drawable.emptyheart).getConstantState())) {
                    favouritesButton.setBackgroundResource(R.drawable.filledheart);
                    myUser.favouriteLocations.add(currentLocation.getName());
                } else if (Objects.equals(favouritesButton.getBackground().getConstantState(), getResources().getDrawable(R.drawable.filledheart).getConstantState())) {
                    favouritesButton.setBackgroundResource(R.drawable.emptyheart);
                    myUser.favouriteLocations.remove(currentLocation.getName());
                } else {
                    Log.d("debug", "Error");
                }
                firebase.userReference.set(myUser);
            }
        });
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
