package orbital.gns.tpspacefinder.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.Classes.UsersAdapter;
import orbital.gns.tpspacefinder.R;

public class FavouritesActivity extends AppCompatActivity {

//    The recycler view in the layout
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;

//    Package containing the firebase resources
    private FirebasePackage firebase;

//    User information
    private User myUser;
    private ArrayList<String> usersFavouriteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebase = new FirebasePackage();

        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.recyclerView);
//        Bundle bundle = this.getIntent().getExtras();
//        assert bundle != null;
//        myUser = (User) bundle.getSerializable("user");
//        assert myUser != null;

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouritesActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebase.userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myUser = documentSnapshot.toObject(User.class);
                Log.d("debug", String.valueOf(myUser.favouriteLocations));
                assert myUser != null;
                usersFavouriteLocation = new ArrayList<>(myUser.favouriteLocations);
                usersAdapter = new UsersAdapter(FavouritesActivity.this, usersFavouriteLocation);
                recyclerView.setAdapter(usersAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager((FavouritesActivity.this)));
                Log.d("debug", "everything is ok");
            }
        });

    }
}
