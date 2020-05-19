package orbital.gns.tpspacefinder.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;


public class LogoutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private User myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

//        Retrieves the username from the action page.
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        myUser = (User) bundle.getSerializable("user");

        assert myUser != null;
        final String goodbyeMessage = "Goodbye " + myUser.getUsername();

        mAuth = FirebaseAuth.getInstance();

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), goodbyeMessage, Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                assert mAuth == null;
                finish();
            }
        });


    }
}
