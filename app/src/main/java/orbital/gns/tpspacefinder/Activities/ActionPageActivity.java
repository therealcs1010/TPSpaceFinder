package orbital.gns.tpspacefinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class ActionPageActivity extends AppCompatActivity {

    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_page);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(this, StartActivity.class);
            Log.d("debug", "User not authenticated");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            CircleMenu circleMenu = findViewById(R.id.circleMenu);
            circleMenu.setMainMenu(R.color.fadedwhite, R.drawable.menu, R.drawable.menu)
                    .addSubMenu(R.color.fadedpalette1, R.drawable.location)
                    .addSubMenu(R.color.fadedpalette2, R.drawable.signout)
                    .addSubMenu(R.color.fadedpalette3, R.drawable.search)
                    .addSubMenu(R.color.fadedpalette4, R.drawable.favourites)
                    .addSubMenu(R.color.fadedpalette5, R.drawable.profile)
                    .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                        @Override
                        public void onMenuSelected(int index) {
//                            Map
                            if (index == 0) {
                                transitToOtherActivity(MapActivity.class);
//                            Feedback
                            } else if (index == 1) {
                                Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
                                intent.putExtra("username", myUser.getUsername());
                                startActivity(intent);
//                            Favorites
                            } else if (index == 2) {
                                transitToOtherActivity(SearchBarActivity.class);
//                            Crowd Checker
                            } else if (index == 3) {
                                transitToOtherActivity(FavouritesActivity.class);
                            } else if (index == 4) {
                                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                                intent.putExtra("userInfo", myUser);
                                startActivity(intent);
                            }

                        }
                    });
//        Retrieves user's information from the database.
            getUserInformationFromFirebase(user);

        }
    }

    /**
     * Get user's information from Firebase.
     * @param user The user who is logged in.
     */
    private void getUserInformationFromFirebase(FirebaseUser user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users/" + user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("debug", dataSnapshot.toString());
                  myUser = dataSnapshot.getValue(User.class);
//                    Log.d("debug", currentUser.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("debug", "Failed to read value");
            }
        });
    }

    /**
     * Basic function to transition to another activity.
     * @param next The next class to be used.
     */
    private void transitToOtherActivity(Class next) {
        Intent intent = new Intent(this, next);
        startActivity(intent);
    }


}
