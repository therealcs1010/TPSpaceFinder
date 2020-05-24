package orbital.gns.tpspacefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;
import orbital.gns.tpspacefinder.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(ActionPageActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundColor(Color.parseColor("#1a1b29"))
//                .withHeaderText("Header")
//                .withFooterText("Footer")
                .withBeforeLogoText("Find seats easily with")
                .withLogo(R.drawable.logo);
//                .withAfterLogoText("After Logo Text");


//        config.getHeaderTextView().setTextColor(Color.WHITE);
//        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextSize(20);
//        config.getAfterLogoTextView().setTextColor(Color.WHITE);
        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
