package orbital.gns.tpspacefinder.Classes;


import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import orbital.gns.tpspacefinder.R;

public class LocationPackage {
    public HashMap<String, LatLng> allLocationsLatLng;
    public HashMap<String, Integer> allLocationsDrawable;
    public LocationPackage() {
        allLocationsLatLng = new HashMap<>();
        allLocationsLatLng.put("The Short Circuit", new LatLng(1.346270, 103.931577));
        allLocationsLatLng.put("The Bread Board", new LatLng(1.346772, 103.930154));
        allLocationsLatLng.put("The Business Park",new LatLng(1.344199, 103.933042));
        allLocationsLatLng.put("The Designer Pad",new LatLng(1.345174, 103.931471));
        allLocationsLatLng.put("The Flavours",new LatLng(1.345033, 103.934102));
        allLocationsLatLng.put("TripletS",new LatLng(1.344686, 103.932225));
        allLocationsLatLng.put("McDonald's",new LatLng(1.345318, 103.931810));
        allLocationsLatLng.put("Subway",new LatLng(1.344920, 103.932289));
        allLocationsLatLng.put("Bistro Lab",new LatLng(1.343949, 103.931801));
        allLocationsLatLng.put("Sugarloaf",new LatLng(1.346845, 103.929159));
        allLocationsLatLng.put("Canopy Coffee Club Café",new LatLng(1.345140, 103.935490));
        allLocationsLatLng.put("The Top Table",new LatLng(1.346483, 103.929196));
        allLocationsDrawable = new HashMap<>();
        allLocationsDrawable.put("The Short Circuit", R.drawable.shortcircuit);
        allLocationsDrawable.put("The Bread Board", R.drawable.breadboard);
        allLocationsDrawable.put("The Business Park",R.drawable.businesspark);
        allLocationsDrawable.put("The Designer Pad",R.drawable.designerpad);
        allLocationsDrawable.put("The Flavours",R.drawable.flavours);
        allLocationsDrawable.put("TripletS", R.drawable.triplets);
        allLocationsDrawable.put("McDonald's",R.drawable.mcdonalds);
        allLocationsDrawable.put("Subway",R.drawable.subway);
        allLocationsDrawable.put("Bistro Lab",R.drawable.bistrolab);
        allLocationsDrawable.put("Sugarloaf",R.drawable.sugar);
        allLocationsDrawable.put("Canopy Coffee Club Café", R.drawable.canopy);
        allLocationsDrawable.put("The Top Table",R.drawable.toptable);

    }

}
