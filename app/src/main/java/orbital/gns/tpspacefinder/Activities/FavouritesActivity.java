package orbital.gns.tpspacefinder.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.Classes.UsersAdapter;
import orbital.gns.tpspacefinder.R;

public class FavouritesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

//    The recycler view in the layout
    private RecyclerView recyclerView;
    private SearchView searchView;
    private UsersAdapter usersAdapter;
    private ImageButton backButton;

//    Firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;

//    User information
    private User myUser;
    private ArrayList<String> usersFavouriteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        setUpLayout();
        getUserInformationFromDatabase();

        usersFavouriteLocation = new ArrayList<>();
        usersFavouriteLocation.add("The Short Circuit");
        usersFavouriteLocation.add("The Bread Board");
        usersFavouriteLocation.add("The Business Park");
        usersFavouriteLocation.add("The Designer Pad");
        usersFavouriteLocation.add("The Flavours");
        usersFavouriteLocation.add("TripletS");

        usersAdapter = new UsersAdapter(this, usersFavouriteLocation);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        searchView.setOnQueryTextListener(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpLayout() {
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        backButton = findViewById(R.id.backButton);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
    }


    private void getUserInformationFromDatabase() {
        database.getReference("users/" + user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("debug", dataSnapshot.toString());
                myUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("debug", "Failed to read value");
            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        usersAdapter.getFilter().filter(text);
        return false;
    }

}
