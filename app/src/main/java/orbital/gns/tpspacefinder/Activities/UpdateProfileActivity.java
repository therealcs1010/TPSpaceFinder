package orbital.gns.tpspacefinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class UpdateProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myReference;
    private User myUser;

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
//        Authenticate
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;

//        Retrieve user information
        myUser = (User) Objects.requireNonNull(getIntent().getExtras()).get("userInfo");
        database = FirebaseDatabase.getInstance();
        myReference = database.getReference("users");

        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

        final ToggleButton genderField = findViewById(R.id.genderButton);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UpdateProfileActivity.super.onBackPressed();
            }
        });

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyProfile(genderField);
            }
        });

        initializeFields(genderField);


    }

    /**
     * Fills in all the parameters with the user's profile information.
     * @param genderField The gender button showing either male or female.
     */
    private void initializeFields(ToggleButton genderField) {
        if (myUser.getGender().equals("Male")) {
            genderField.setChecked(false);
        } else {
            genderField.setChecked(true);
        }
        nameField.setText(myUser.getUsername());
        emailField.setText(myUser.getEmail());
        passwordField.setText(myUser.getPassword());
    }

    private void updateMyProfile(ToggleButton genderField) {
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String gender;
        if (genderField.isChecked()) {
            gender = "Female";
        } else {
            gender = "Male";
        }
        myUser.setEmail(email);
        myUser.setPassword(password);
        myUser.setUsername(name);
        myUser.setGender(gender);

        Log.d("debug", "smthin failed");
        myReference.child(user.getUid()).setValue(myUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void T) {
                //Do whatever
                Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
