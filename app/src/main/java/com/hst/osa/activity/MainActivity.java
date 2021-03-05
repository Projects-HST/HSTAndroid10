package com.hst.osa.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hst.osa.R;
import com.hst.osa.fragment.CategoryFragment;
import com.hst.osa.fragment.DashboardFragment;
import com.hst.osa.interfaces.OnBackPressedListener;
import com.hst.osa.utils.OSAValidator;
import com.hst.osa.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnBackPressedListener {

    Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout sideDash, sideProfile, sideCat, sideWish, sideOrder, sideWallet, sideAddress, sideSettings, sideLogout;
    private TextView name, mailId;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView profilePic;
    NavigationView navigationView;
    public String page = "", productID = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        page = getIntent().getExtras().getString("page");
//        productID = getIntent().getExtras().getString("productObj");
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        initializeNavigationDrawer();
        initializeIDs();
    }

    private void initializeIDs() {

        navigationView = findViewById(R.id.nav_view);
        profilePic = navigationView.getHeaderView(0).findViewById(R.id.user_img);
        name = navigationView.getHeaderView(0).findViewById(R.id.full_name);
        mailId = navigationView.getHeaderView(0).findViewById(R.id.area);
        if(PreferenceStorage.getName(this).equalsIgnoreCase("") || PreferenceStorage.getName(this).isEmpty()) {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("page", "dash");
                    i.putExtra("productObj", "");
                    startActivity(i);
                }
            });
        } else {
            name.setText(PreferenceStorage.getName(this));
            mailId.setText(PreferenceStorage.getEmail(this));
        }

        if (OSAValidator.checkNullString(PreferenceStorage.getProfilePic(this))&&(!PreferenceStorage.getProfilePic(this).isEmpty())) {
            Picasso.get().load(PreferenceStorage.getProfilePic(this)).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.ic_profile);
        }

        sideDash = navigationView.getHeaderView(0).findViewById(R.id.side_dashboard);
        sideProfile = navigationView.getHeaderView(0).findViewById(R.id.side_profile);
        sideCat = navigationView.getHeaderView(0).findViewById(R.id.side_category);
        sideOrder = navigationView.getHeaderView(0).findViewById(R.id.side_order_history);
        sideWallet = navigationView.getHeaderView(0).findViewById(R.id.side_wallet);
        sideAddress = navigationView.getHeaderView(0).findViewById(R.id.side_address);
        sideSettings = navigationView.getHeaderView(0).findViewById(R.id.side_settings);
        sideLogout = navigationView.getHeaderView(0).findViewById(R.id.side_logout);

        sideDash.setOnClickListener(this);
        sideProfile.setOnClickListener(this);
        sideCat.setOnClickListener(this);
        sideOrder.setOnClickListener(this);
        sideWallet.setOnClickListener(this);
        sideAddress.setOnClickListener(this);
        sideSettings.setOnClickListener(this);
        sideLogout.setOnClickListener(this);

        changePage(0);
        if (page.equalsIgnoreCase("product")) {
            Intent homeIntent;
            homeIntent = new Intent(getApplicationContext(), ProductDetailActivity.class);
            homeIntent.putExtra("productObj", productID);
            startActivity(homeIntent);
        }

    }

//    @Override
//    public void onBackPressed() {
//        //Checking for fragment count on backstack
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else if (!doubleBackToExitPressedOnce) {
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
//        } else {
//            super.onBackPressed();
//            return;
//        }
//    }

    public void changePage(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            toolbar.setTitle(getString(R.string.side_menu_dash_title));
            mDrawerLayout.closeDrawers();
            newFragment = new DashboardFragment();
        } else if (position == 1) {
            newFragment = new CategoryFragment();
            toolbar.setTitle(getString(R.string.side_menu_category));
            mDrawerLayout.closeDrawers();
        }
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

    private void logout() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.sign_out));
        alertDialogBuilder.setMessage(getString(R.string.sign_out_alert));
        alertDialogBuilder.setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(alertDialogBuilder.getContext());
                sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();


                Intent homeIntent = new Intent(alertDialogBuilder.getContext(), SplashscreenActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void initializeNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
//                String userProfileName = PreferenceStorage.getName(getApplicationContext());
//                String url = PreferenceStorage.getUserPicture(ParentDashBoardActivity.this);
//
//                Log.d(TAG, "user name value" + userProfileName);
//                if ((userProfileName != null) && !userProfileName.isEmpty()) {
//                    String[] splitStr = userProfileName.split("\\s+");
//                    navUserProfileName.setText("Hi, " + splitStr[0]);
//                }
//
//                if (((url != null) && !(url.isEmpty())) && !(url.equalsIgnoreCase(mCurrentUserProfileUrl))) {
//                    Log.d(TAG, "image url is " + url);
//                    mCurrentUserProfileUrl = url;
//                    Picasso.with(ParentDashBoardActivity.this).load(url).noPlaceholder().error(R.drawable.ab_logo).into(imgNavProfileImage);
//                }
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hambugger);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        // enable ActionBar app icon to behave as action to toggle nav drawer
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Using in Base Fragment
     */
    public ActionBarDrawerToggle getToggle() {
        return mDrawerToggle;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof OnBackPressedListener) {
            ((OnBackPressedListener) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == sideDash) {
            changePage(0);
        }if (view == sideProfile) {
        }if (view == sideCat) {
            Intent i = new Intent(this, CategoryActivity.class);
            startActivity(i);
//            changePage(1);
        }if (view == sideOrder) {

        }if (view == sideWallet) {

        }if (view == sideAddress) {

        }if (view == sideSettings) {

        }if (view == sideLogout) {
            logout();
        }
    }
}