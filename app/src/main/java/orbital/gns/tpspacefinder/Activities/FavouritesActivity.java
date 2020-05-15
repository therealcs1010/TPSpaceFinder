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

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.Classes.UsersAdapter;
import orbital.gns.tpspacefinder.R;

public class FavouritesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

//    The recycler view in the layout
    private RecyclerView recyclerView;
    private SearchView searchView;
    private UsersAdapter usersAdapter;
    private ImageButton backButton;

//    Package containing the firebase resources
    private FirebasePackage firebase;

//    User information
    private User myUser;
    private ArrayList<String> usersFavouriteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        backButton = findViewById(R.id.backButton);

        firebase = new FirebasePackage();

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
