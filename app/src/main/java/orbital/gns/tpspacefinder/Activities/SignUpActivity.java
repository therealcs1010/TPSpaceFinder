package orbital.gns.tpspacefinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();


//        Listener for the gender button
        final ToggleButton genderButton = findViewById(R.id.genderButton);

//        Listener for the back button to return to the starting page
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        Listener for the sign up button
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSignUpDetails(genderButton);
            }
        });

    }


    /**
     * Gets the sign up details from the parameters and attempts to authenticate with Firebase.
     * If authentication is successful, then it will show the action page.
     * @param genderButton Button assigned to determine the gender of the user.
     */
    private void getSignUpDetails(ToggleButton genderButton) {
//        Parameters for the different objects in the layout.
        EditText nameField = findViewById(R.id.nameField);
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);

//        Retrieves the name, email and password from the user.
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String gender;
        if (genderButton.isChecked()) {
            gender = "Female";
        } else {
            gender = "Male";
        }

        if (getAuthentication(name, email, password)) {
//        If account is successfully created, then can proceed to action page.
            uploadDetailsToFirebase(name, email, password, gender);
            Intent intent = new Intent(getApplicationContext(), ActionPageActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * Uploads the details to Firebase Database for easy referencing.
     * @param name name provided by the user.
     * @param email email provided by the user.
     * @param password password provided by the user.
     * @param gender gender provided by the user.
     */
    private void uploadDetailsToFirebase(String name, String email, String password, String gender) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Log.d("debug", "No such user found");
            return;
        }
        String uid = user.getUid();
        DatabaseReference myRef = database.getReference("/users/" + uid);
        myRef.setValue(new User(name, email, password, gender));
        Log.d("debug", "Uploaded to firebase database");
    }

    /**
     * Attempts to authenticate with Firebase.
     * @param name name filled by the user.
     * @param email email filled by the user.
     * @param password password filled by the user.
     * @return true if successful authentication, false if otherwise.
     */
    private boolean getAuthentication(String name, String email, String password) {
        //        Ensures that the fields are all complete before proceeding
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            return false;
        }

//        Get the details of the user and authenticate with Firebase.
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                If successful, it will proceed to the action page.
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
//                    Or else it will return and print an error message.
                } else {
                    Log.d("debug", "Task failed");
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password);

        user = mAuth.getCurrentUser();
        if (user != null)
        Log.d("debug", user.getUid());
        return user != null;

    }
}
