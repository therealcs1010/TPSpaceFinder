package orbital.gns.tpspacefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class LogoutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

//        Retrieves the username from the action page.
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        final String goodbyeMessage = "Goodbye " + username;

        mAuth = FirebaseAuth.getInstance();

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), goodbyeMessage, Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                assert mAuth == null;
                finishAffinity();
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
