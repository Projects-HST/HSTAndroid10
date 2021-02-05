package com.hst.osa.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hst.osa.R;
import com.hst.osa.utils.OSAValidator;
import com.hst.osa.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private TextView name, area;
    private boolean doubleBackToExitPressedOnce = false;
    private CardView profilePic;
    NavigationView navigationView;
    String page = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        initializeNavigationDrawer();
        initializeIDs();
    }

    private void initializeIDs() {

        navigationView = findViewById(R.id.nav_view);
        profilePic = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        name = navigationView.getHeaderView(0).findViewById(R.id.full_name);
        area = navigationView.getHeaderView(0).findViewById(R.id.area);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        if (OSAValidator.checkNullString(PreferenceStorage.getProfilePic(this))) {
//            Picasso.get().load(PreferenceStorage.getProfilePic(this)).into(profilePic);
        } else {
//            profilePic.setImageResource(R.drawable.ic_profile);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_dash:
//                        changePage(0);
                        break;
                    case R.id.nav_profile:
//                        showItems();
                        break;
                    case R.id.nav_category:
//                        changePage(1);
                        break;
                    case R.id.nav_wishlist:
//                        changePage(2);
                        break;
                    case R.id.nav_order:
//                        changePage(3);
                        break;
                    case R.id.nav_wallet:
//                        changePage(4);
                        break;
                    case R.id.nav_address:
//                        changePage(5);
                        break;
                    case R.id.nav_settings:
//                        changePage(6);
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                }
                return true;
            }
        });
//        if (page.equalsIgnoreCase("settings")) {
//            changePage(6);
//            navigationView.getMenu().getItem(7).setChecked(true);
//        } else {
//            changePage(0);
//            navigationView.getMenu().getItem(0).setChecked(true);
//        }

//        if (PreferenceStorage.getUserRole(this).equalsIgnoreCase("1")) {
//            navigationView.getMenu().getItem(5).setVisible(true);
//        } else {
//            navigationView.getMenu().getItem(5).setVisible(false);
//        }

//        callGetSubCategoryService();

    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    public void changePage(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            toolbar.setTitle(getString(R.string.side_menu_dash));
            mDrawerLayout.closeDrawers();
//            newFragment = new DashboardFragment();
        } else if (position == 1) {
//            newFragment = new ConstituentFragment();
//            toolbar.setTitle(getString(R.string.constituent_title));
            mDrawerLayout.closeDrawers();
        } else if (position == 2) {
//            newFragment = new MeetingsFragment();
//            toolbar.setTitle(getString(R.string.meeting));
            mDrawerLayout.closeDrawers();
        } else if (position == 3) {
//            newFragment = new GrievanceFragment();
//            toolbar.setTitle(getString(R.string.grievance_title));
            mDrawerLayout.closeDrawers();
        } else if (position == 4) {
//            newFragment = new StaffFragment();
//            toolbar.setTitle(getString(R.string.side_menu_staff));
            mDrawerLayout.closeDrawers();
        } else if (position == 5) {
//            newFragment = new ReportFragment();
//            toolbar.setTitle(getString(R.string.side_menu_report));
            mDrawerLayout.closeDrawers();
        } else if (position == 6) {
//            newFragment = new SettingsFragment();
//            toolbar.setTitle(getString(R.string.side_menu_settings));
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
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hambugger);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

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


}