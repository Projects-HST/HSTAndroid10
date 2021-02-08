package com.hst.osa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hst.osa.R;
import com.hst.osa.bean.database.SQLiteHelper;
import com.hst.osa.utils.FirstTimePreference;
import com.hst.osa.utils.PreferenceStorage;


public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = WelcomeActivity.class.getName();

    private int[] layouts;
    private TextView btnSkip, btnNext;
    //    private static String[] PERMISSIONS_ALL = { Manifest.permission.READ_CALENDAR,
//            Manifest.permission.WRITE_CALENDAR, Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int REQUEST_PERMISSION_All = 111;
    SQLiteHelper database;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new SQLiteHelper(getApplicationContext());

        FirstTimePreference prefFirstTime = new FirstTimePreference(getApplicationContext());

        if (prefFirstTime.runTheFirstTime("FirstTimePermit")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                requestAllPermissions();
            }
        }

        boolean haspreferences = PreferenceStorage.isFirstTimeLaunch(getApplicationContext());
        if (!haspreferences) {
            launchHomeScreen();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        btnSkip = (TextView) findViewById(R.id.btn_skip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        PreferenceStorage.setFirstTimeLaunch(getApplicationContext(), false);
        database.app_info_check_insert("Y");
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

}