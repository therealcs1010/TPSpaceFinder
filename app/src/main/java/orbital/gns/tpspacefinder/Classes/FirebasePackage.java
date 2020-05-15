package orbital.gns.tpspacefinder.Classes;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebasePackage {

    private FirebaseAuth mAuth;
    public FirebaseFirestore database;
    private FirebaseUser user;
    private String uid;
    public Location myLocation;
    public DocumentReference userReference;

    public FirebasePackage() {
        mAuth = FirebaseAuth.getInstance();
        Log.d("debug", "wo1t");
        user = mAuth.getCurrentUser();
        Log.d("debug", "wo2t");
        database = FirebaseFirestore.getInstance();
        Log.d("debug", "wot3");
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
        return user != null;
    }

    public String getUid() {
        return uid;
    }

    public Location getmyLocation(String location) {
        database.collection("Locations").document(location).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myLocation = documentSnapshot.toObject(Location.class);
            }
        });
        return myLocation;
    }

}
