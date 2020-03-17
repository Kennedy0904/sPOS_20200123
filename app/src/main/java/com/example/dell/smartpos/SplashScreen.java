package com.example.dell.smartpos;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pax.dal.IDAL;
import com.pax.dal.entity.ENavigationKey;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initialize language
        GlobalFunction.changeLanguage(SplashScreen.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Return 'True' if user didn't logged out
                Boolean loginStatus = GlobalFunction.getLoginStatus(SplashScreen.this);

                if (loginStatus) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIMEOUT);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (GlobalFunction.onKeyDownEvent(SplashScreen.this, keyCode, event, getPackageManager())) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (GlobalFunction.isFDMS_BASE24Installed(getPackageManager())) {
            Toast.makeText(SplashScreen.this, "Not allowed!", Toast.LENGTH_SHORT).show();
        }
    }
}
