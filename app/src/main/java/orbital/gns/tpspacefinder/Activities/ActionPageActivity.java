package orbital.gns.tpspacefinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.LocationPackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class ActionPageActivity extends AppCompatActivity {

    private FirebasePackage firebase;
    private Intent intent;
    private User myUser = new User();
    private String result = "";
    private LocationPackage locationEnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new FirebasePackage();
        locationEnum = new LocationPackage();

        if (!firebase.isAuthenticated()) {
            intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            transitToOtherActivity(intent, true);
            finish();
        }
        else {
            firebase.userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myUser = documentSnapshot.toObject(User.class);
                }
            });
            setContentView(R.layout.activity_action_page);
            CircleMenu circleMenu = findViewById(R.id.circleMenu);
            circleMenu.setMainMenu(R.color.fadedwhite, R.drawable.menu, R.drawable.menu)
                    .addSubMenu(R.color.fadedpalette2, R.drawable.signout)
                    .addSubMenu(R.color.fadedpalette3, R.drawable.search)
                    .addSubMenu(R.color.fadedpalette4, R.drawable.favourites)
                    .addSubMenu(R.color.fadedpalette5, R.drawable.profile)
                    .addSubMenu(R.color.fadedpalette1, R.drawable.camera)
                    .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                        @Override
                        public void onMenuSelected(int index) {
//                            Feedback
                            if (index == 0) {
                                intent = new Intent(getApplicationContext(), LogoutActivity.class);
//                            Search for a specific location
                            } else if (index == 1) {
                                intent = new Intent(getApplicationContext(), SearchBarActivity.class);
//                            Favourite locations
                            } else if (index == 2) {
                                intent = new Intent(getApplicationContext(), FavouritesActivity.class);
//                            Updating profile
                            } else if (index == 3) {
                                intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
//                            Scanning
                            } else if (index == 4) {
                                Log.d("debug", "yeet");
                                intent = new Intent(getApplicationContext(), QRCodeActivity.class);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", myUser);
                            intent.putExtras(bundle);
                            transitToOtherActivity(intent, false);
                        }
                    });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firebase.isAuthenticated()) {
            intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            transitToOtherActivity(intent, true);
            finish();
        }
    }

    /**
     * Basic function to transition to another activity.
     * @param intent The intent to be used.
     * @param finish A variable to check if finish has to be called.
     */
    private void transitToOtherActivity(Intent intent, boolean finish) {
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

}
