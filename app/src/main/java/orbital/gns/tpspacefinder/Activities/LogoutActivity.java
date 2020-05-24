package orbital.gns.tpspacefinder.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;


public class LogoutActivity extends AppCompatActivity {

    private FirebasePackage firebase;
    private User myUser;

    private EditText feedbackTextInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new FirebasePackage();

//        Retrieves the username from the action page.
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        myUser = (User) bundle.getSerializable("user");
        setContentView(R.layout.activity_logout);
        feedbackTextInput = findViewById(R.id.feedbackTextInput);
        assert myUser != null;

        final String goodbyeMessage = "Goodbye " + myUser.getUsername();


        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = feedbackTextInput.getText().toString();
                if (value == null) {
                    value = "";
                }
                DocumentReference ref = firebase.database.collection("Feedback").document("Feedbacks");
                ref.update("userFeedbacks", FieldValue.arrayUnion(value)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), goodbyeMessage, Toast.LENGTH_SHORT).show();
                        firebase.mAuth.signOut();
                        finish();
                    }
                });
            }
        });
    }
}