package orbital.gns.tpspacefinder.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import orbital.gns.tpspacefinder.Classes.Location;
import orbital.gns.tpspacefinder.Classes.LocationPackage;
import orbital.gns.tpspacefinder.R;


public class LocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Location currentLocation;
    private ImageView locationImageView;
    private TextView locationName;
    private MapView mapView;
    private GoogleMap gmap;
    private LocationPackage locationPackage;
    private LatLng currentLatLng = new LatLng(0,0);

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPackage = new LocationPackage();
        setContentView(R.layout.activity_location_details);
        setItemsToIds();
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;

        currentLocation = (Location) bundle.getSerializable("CurrentLocationDetails");
        currentLatLng = locationPackage.allLocations.get(currentLocation.getName());
        int locationResource = (int) bundle.getSerializable("Image");

        locationImageView.setImageResource(locationResource);
        locationName.setText(currentLocation.getName());
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map);
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
