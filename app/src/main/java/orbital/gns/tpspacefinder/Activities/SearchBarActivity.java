package orbital.gns.tpspacefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.util.ArrayList;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.LocationPackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.Classes.UsersAdapter;
import orbital.gns.tpspacefinder.R;

public class SearchBarActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private FirebasePackage firebase;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private UsersAdapter usersAdapter;

    private User myUser;
    private ArrayList<String> allLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        LocationPackage locationEnum = new LocationPackage();
        setUpLayout();

        allLocations = new ArrayList<>(locationEnum.allLocationsLatLng.keySet());
        usersAdapter = new UsersAdapter(this, allLocations);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        searchView.setOnQueryTextListener(this);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBarActivity.super.onBackPressed();
            }
        });
    }

    private void setUpLayout() {
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        firebase = new FirebasePackage();
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
