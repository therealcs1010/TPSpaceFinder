package orbital.gns.tpspacefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.Classes.UsersAdapter;
import orbital.gns.tpspacefinder.R;

public class SearchBarActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private UsersAdapter usersAdapter;
    private ImageButton backButton;

    private User myUser;
    private ArrayList<String> allLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        setUpLayout();

        allLocations = new ArrayList<>();
        allLocations.add("The Short Circuit");
        allLocations.add("The Bread Board");
        allLocations.add("The Business Park");
        allLocations.add("The Designer Pad");
        allLocations.add("The Flavours");
        allLocations.add("TripletS");
        allLocations.add("McDonald's");
        allLocations.add("Subway");
        allLocations.add("Bistro Lab");
        allLocations.add("Sugarloaf");
        allLocations.add("Canopy Coffee Club Café");
        allLocations.add("The Top Table");

        usersAdapter = new UsersAdapter(this, allLocations);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        searchView.setOnQueryTextListener(this);





        backButton.setOnClickListener(new View.OnClickListener(){

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        usersAdapter.getFilter().filter(newText);
        return false;
    }
}
