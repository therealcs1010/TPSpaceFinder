package orbital.gns.tpspacefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import orbital.gns.tpspacefinder.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        Button for signing up
        Button signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity(SignUpActivity.class);
            }
        });

//        Button for logging in
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity(LoginActivity.class);
            }
        });
    }

    /**
     * Action to transition to the next activity.
     * @param nextClass The class to transition to.
     */
    private void getActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        startActivity(intent);
        finish();
    }


}
