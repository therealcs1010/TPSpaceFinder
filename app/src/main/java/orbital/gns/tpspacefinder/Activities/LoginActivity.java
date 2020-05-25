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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import orbital.gns.tpspacefinder.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

//        The button to go back to the starting page

//        The button to login into an account
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoginDetails();
            }
        });
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.super.onBackPressed();
            }
        });
    }


    /**
     * Retrieves login details from the fields and tries to authenticate with firebase.
     */
    private void getLoginDetails() {
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        getAuthenticated(email, password);
    }

    /**
     * Method of authentication with firebase using an on-complete listener.
     * @param email Email filled by the user.
     * @param password Password filled by the user.
     */
    private void getAuthenticated(String email, String password) {
        //        Ensures that all the fields are filled up.
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            return;
        }
//        Attempt to sign into firebase. If it is complete, then it will log the user in and move to the action page.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("debug", "Successful Login");
                            Intent intent = new Intent(getApplicationContext(), ActionPageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            LoginActivity.super.finish();
                        } else {
                            Log.d("debug", "Wrong email or password");
                            Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}

