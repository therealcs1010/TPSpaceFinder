package orbital.gns.tpspacefinder.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import orbital.gns.tpspacefinder.Classes.FirebasePackage;
import orbital.gns.tpspacefinder.Classes.Location;
import orbital.gns.tpspacefinder.Classes.LocationPackage;
import orbital.gns.tpspacefinder.Classes.User;
import orbital.gns.tpspacefinder.R;

public class QRCodeActivity extends AppCompatActivity {

    private FirebasePackage firebase;
    private LocationPackage locationEnum;
    private String result = "";
    private User myUser;
    private String [] stringResultArray;
    private Button scanButton;
    String CHANNEL_ID = "1001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Request permission to use the cameras
        createNotificationChannel();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
        firebase = new FirebasePackage();
//        Retrieve user information
        retrieveUserInformationFromFireStore();

        locationEnum = new LocationPackage();
        setContentView(R.layout.activity_qrcode);
        Log.d("debug", "what1");

        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(QRCodeActivity.this);
                Log.d("debug", "what");
                intentIntegrator.initiateScan();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeActivity.super.onBackPressed();
            }
        });

    }

    private void retrieveUserInformationFromFireStore() {
        firebase.userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myUser = documentSnapshot.toObject(User.class);
                buildAlertDialog();
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Spc";
            String description = "wot";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Builds the alert dialog for when user already has an outstanding location. Retrieves the old location
     * and sets it back to false, then it sets the new location to true, followed by updating the user information in firestore.
     */
    private void buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You already have a seat. Do you wish to override your old choice?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String[] oldLocationArr = myUser.seatTaken.split("_");
                        updateLocation(oldLocationArr, false);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        if (myUser.seatTaken != "") {
            AlertDialog alert = builder.create();
            alert.setTitle("");
            alert.show();
        }
    }

    /**
     * This function will be called upon scanning a QR code, then books the spot for the user.
     * @param requestCode The requestcode given by the user.
     * @param resultCode The result of getting the permission.
     * @param data The intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (checkIfLocationIsValid(intentResult)) {
            bookLocationForUser();
        }

    }

    /**
     * Books the current seat for the user. It checks if the user has an outstanding seat. If so, it will show an alert dialog,
     * else it will simply just update the location accordingly.
     */
    private void bookLocationForUser() {
        firebase.getLocationReference(stringResultArray[0]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                updateLocation(stringResultArray, true);
            }
        });
    }

    /**
     * Updates the location to firestore. Checks if its a take seat or return seat action, then it updates the local location data,
     * before it uploads to firestore. Similarly, it updates myUser before uploading to firestore.
     * @param res the String array containing the location name, and the index.
     * @param takeSeat a boolean expression determining if its a take seat or return seat expression.
     */
    private void updateLocation(final String [] res, final boolean takeSeat) {
        firebase.getLocationReference(res[0]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Location currentLocation = documentSnapshot.toObject(Location.class);
                assert currentLocation != null;
                if (takeSeat) {
                    if (currentLocation.getSeats(res[1])) {
                        firebase.database.collection("Locations").document(currentLocation.getName()).set(currentLocation);
                        myUser.seatTaken = result;
                        firebase.userReference.set(myUser);
                        String seatString = "You have selected " + currentLocation.getName() + " seat no : " + res[1];
                        Toast.makeText(getApplicationContext(), seatString, Toast.LENGTH_SHORT).show();
                        pushNotificationToDevice(seatString);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong selecting seat.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (currentLocation.returnSeats(res[1])) {
                        firebase.database.collection("Locations").document(currentLocation.getName()).set(currentLocation);
                        myUser.seatTaken = "";
                        firebase.userReference.set(myUser);
                        Log.d("debug", "returned");
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong returning seat.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void pushNotificationToDevice(String seatString) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentText(seatString)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(seatString))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(100, builder.build());
    }

    /**
     * Function to check if the scanned QR code is valid. It checks if the contents of the QR code is valid.
     * Then, it checks if the results of the QR code is valid by splitting the string by "_", where the first part
     * is the location name, and the second part is the seat number. It checks the length to ensure it is a valid QR_code,
     * then cross reference with firestore to ensure that the location is valid.
     * @param intentResult The IntentResult
     * @return true if valid. false otherwise.
     */
    private boolean checkIfLocationIsValid(IntentResult intentResult) {
        if (intentResult == null) {
            return false;
        }
        if (intentResult.getContents() == null) {
            return false;
        }
        result = intentResult.getContents();
        Log.d("debug", result);
        stringResultArray = result.split("_");
        if (stringResultArray.length != 2) {
            return false;
        }
        String location = stringResultArray[0];
        return locationEnum.allLocationsLatLng.containsKey(location);
    }

}