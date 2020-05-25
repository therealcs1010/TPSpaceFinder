package orbital.gns.tpspacefinder.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class UpdateProfileActivity extends AppCompatActivity {

    private FirebasePackage firebase;
    private FirebaseUser firebaseUser;
    private User myUser;

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private ImageButton backButton;
    private ToggleButton genderField;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Authenticate
        firebase = new FirebasePackage();
        firebaseUser = firebase.mAuth.getCurrentUser();
//        Retrieve user information
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        myUser = (User) bundle.getSerializable("user");

        setContentView(R.layout.activity_update_profile);
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        genderField = findViewById(R.id.genderButton);
        updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyProfile(genderField);
            }
        });
        initializeFields();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfileActivity.super.onBackPressed();
            }
        });
    }


    /**
     * Fills in all the parameters with the user's profile information.
     */
    private void initializeFields() {
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
        Toast.makeText(getApplicationContext(), "Updating information...Please do not leave this page", Toast.LENGTH_SHORT).show();
        final String name = nameField.getText().toString();
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        final String gender;
        if (genderField.isChecked()) {
            gender = "Female";
        } else {
            gender = "Male";
        }
        myUser.setEmail(email);
        myUser.setPassword(password);
        myUser.setUsername(name);
        myUser.setGender(gender);
        firebase.database.collection("Users").document(firebase.getUid()).set(myUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void T) {
                firebaseUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }

}
