package orbital.gns.tpspacefinder.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebasePackage {

    public FirebaseAuth mAuth;
    public FirebaseFirestore database;
    private FirebaseUser user;
    private String uid;
    public Location myLocation;
    public DocumentReference userReference;

    public FirebasePackage() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        if (user == null) {
            uid = "";
            userReference = null;
        } else {
            uid = user.getUid();
            userReference = database.collection("Users").document(uid);
        }
        myLocation = new Location();
    }

    public boolean isAuthenticated() {
        user = mAuth.getCurrentUser();
        return user != null;
    }

    public String getUid() {
        return uid;
    }

    public DocumentReference getLocationReference(String location) {
        return database.collection("Locations").document(location);
    }
}
