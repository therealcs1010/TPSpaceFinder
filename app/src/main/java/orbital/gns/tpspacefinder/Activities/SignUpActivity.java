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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String name, email, password, gender, confirmPassword;

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
        EditText confirmPassWordField = findViewById(R.id.confirmPasswordField);

//        Retrieves the name, email and password from the user.
        name = nameField.getText().toString();
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        confirmPassword = confirmPassWordField.getText().toString();

        if (genderButton.isChecked()) {
            gender = "Female";
        } else {
            gender = "Male";
        }

        getAuthentication();
    }


    /**
     * Attempts to authenticate with Firebase. Checks if the username, email and password field is empty,
     * then checks if the password matches. Then ensures that the password is at least 6 characters. If so, attempts to
     * create a new user.
     */
    private void getAuthentication() {
        //        Ensures that the fields are all complete before proceeding
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || !password.contentEquals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Please use a longer password", Toast.LENGTH_SHORT).show();
            return;
        }

//        Get the details of the user and authenticate with Firebase.
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                If successful, it will proceed to the action page.
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Sign up approved. Please wait a second.", Toast.LENGTH_SHORT).show();
                    uploadDetailsToFirebase();
                    //                    Or else it will return and print an error message.
                } else {
                    Log.d("debug", "Task failed");
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Uploads the details to Firebase Firestore for easy referencing.
     */
    private void uploadDetailsToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        User currentUser = new User(name, email, password, gender);
//        HashMap<String, Object> firestoreDocument = currentUser.getfireStoreDocument();
        db.collection("Users").document(uid).set(currentUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), ActionPageActivity.class);
                startActivity(intent);
                finish();
                Log.d("debug", "yay");
            }
        });
        Log.d("debug", "Uploaded to firebase database");
    }

}
