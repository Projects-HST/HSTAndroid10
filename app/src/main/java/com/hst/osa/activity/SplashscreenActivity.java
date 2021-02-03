package com.hst.osa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import androidx.appcompat.app.AppCompatActivity;
import com.hst.osa.R;

public class SplashscreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String sh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashscreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

        hashFromSHA1();
    }

    public void hashFromSHA1() {
        sh = "28:D6:E7:CF:CC:66:02:16:B1:97:3D:9A:FE:E7:8E:CB:20:1B:DE:03";
        String[] arr = sh.split(":");
        byte[] byteArr = new  byte[arr.length];

        for (int i = 0; i< arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }
}
