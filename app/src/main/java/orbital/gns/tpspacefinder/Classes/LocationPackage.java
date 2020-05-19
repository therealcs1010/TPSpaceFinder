package orbital.gns.tpspacefinder.Classes;


import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class LocationPackage {
    public HashMap<String, LatLng> allLocations;
    public LocationPackage() {
        allLocations = new HashMap<>();
        allLocations.put("The Short Circuit", new LatLng(1.346270, 103.931577));
        allLocations.put("The Bread Board", new LatLng(1.346772, 103.930154));
        allLocations.put("The Business Park",new LatLng(1.344199, 103.933042));
        allLocations.put("The Designer Pad",new LatLng(1.345174, 103.931471));
        allLocations.put("The Flavours",new LatLng(1.345033, 103.934102));
        allLocations.put("TripletS",new LatLng(1.344686, 103.932225));
        allLocations.put("McDonald's",new LatLng(1.345318, 103.931810));
        allLocations.put("Subway",new LatLng(1.344920, 103.932289));
        allLocations.put("Bistro Lab",new LatLng(1.343949, 103.931801));
        allLocations.put("Sugarloaf",new LatLng(1.346845, 103.929159));
        allLocations.put("Canopy Coffee Club Café",new LatLng(1.345140, 103.935490));
        allLocations.put("The Top Table",new LatLng(1.346483, 103.929196));
    }

}
