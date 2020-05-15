package orbital.gns.tpspacefinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class ActionPageActivity extends AppCompatActivity {

    private FirebasePackage firebase;
    private Intent intent;
    private User myUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_page);
        Log.d("debug", "wpotes");
        firebase = new FirebasePackage();
        if (!firebase.isAuthenticated()) {
            Log.d("debug", "User not authenticated");
            intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
//            transitToOtherActivity(intent, true);
        }
        else {
            Log.d("debug", "what");
            firebase.userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myUser = documentSnapshot.toObject(User.class);
                    Log.d("debug", "woo");
                }
            });
        }

        CircleMenu circleMenu = findViewById(R.id.circleMenu);
        circleMenu.setMainMenu(R.color.fadedwhite, R.drawable.menu, R.drawable.menu)
                .addSubMenu(R.color.fadedpalette2, R.drawable.signout)
                .addSubMenu(R.color.fadedpalette3, R.drawable.search)
                .addSubMenu(R.color.fadedpalette4, R.drawable.favourites)
                .addSubMenu(R.color.fadedpalette5, R.drawable.profile)
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
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", myUser);
                        intent.putExtras(bundle);
                        transitToOtherActivity(intent, false);
                    }
                });
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
