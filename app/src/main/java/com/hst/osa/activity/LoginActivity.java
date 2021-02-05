package com.hst.osa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import com.google.android.material.textfield.TextInputLayout;
import com.hst.osa.R;
import com.hst.osa.helpers.AlertDialogHelper;
import com.hst.osa.helpers.ProgressDialogHelper;
import com.hst.osa.interfaces.DialogClickListener;
import com.hst.osa.servicehelpers.ServiceHelper;
import com.hst.osa.serviceinterfaces.IServiceListener;
import com.hst.osa.utils.OSAConstants;
import com.hst.osa.utils.OSAValidator;
import com.hst.osa.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private TextView btnFacebook, btnGoogle;
    private TextInputLayout tiNumber;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private int mSelectedLoginMode = 0;
    private String whichService = "", url = "";

    private Button btnContinue;
    private EditText txtNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tiNumber = findViewById(R.id.ti_mobile_number);
        txtNumber = findViewById(R.id.txt_mobile_number);
        btnContinue = findViewById(R.id.btn_login);
        btnFacebook = findViewById(R.id.btn_fb_login);
        btnGoogle = findViewById(R.id.btn_gg_login);
        btnContinue.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(
                    "com.hst.osa",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

        progressDialogHelper = new ProgressDialogHelper(this);

        facebookLogin();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnContinue){
            if (validateFields()) {
                PreferenceStorage.saveMobileNo(this, txtNumber.getText().toString());
                continueWithNumber();
            }
        }
        if (v == btnGoogle){
            signIn();
        }
        if (v == btnFacebook) {
            loginManager.logInWithReadPermissions(LoginActivity.this, (Arrays.asList("email", "public_profile")));
//            PreferenceStorage.saveLoginMode(this, OSAConstants.FACEBOOK);
//            mSelectedLoginMode = OSAConstants.FACEBOOK;
        }
    }

    private boolean validateFields() {
        if (!OSAValidator.checkNullString(this.txtNumber.getText().toString().trim())) {
            tiNumber.setError(getString(R.string.error_number));
            requestFocus(txtNumber);
            return false;
        }if (!OSAValidator.checkMobileNumLength(this.txtNumber.getText().toString().trim())) {
            tiNumber.setError(getString(R.string.error_number_min));
            requestFocus(txtNumber);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void continueWithNumber() {
        whichService = "mobile";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(OSAConstants.PARAMS_MOBILE_NUMBER, txtNumber.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String serverURL = OSAConstants.BUILD_URL + OSAConstants.MOBILE_VERIFY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
    }

    private void sendGoogleLogin(GoogleSignInAccount account) {
        whichService = "google";
        String name = ""+account.getDisplayName();
        String mail = ""+account.getEmail();
        String photoUrl = "" + account.getPhotoUrl();
        PreferenceStorage.saveSocialNetworkProfilePic(getApplicationContext(), photoUrl);

        String GCMKey = PreferenceStorage.getGCM(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(OSAConstants.PARAMS_USERNAME, name);
            jsonObject.put(OSAConstants.PARAMS_FIRST_NAME, mail);
            jsonObject.put(OSAConstants.PARAMS_LAST_NAME, mail);
            jsonObject.put(OSAConstants.PARAMS_GCM_KEY, GCMKey);
            jsonObject.put(OSAConstants.PARAMS_LOGIN_TYPE, "1");
            jsonObject.put(OSAConstants.PARAMS_MOBILE_TYPE, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String serverURL = OSAConstants.BUILD_URL + OSAConstants.FB_GPLUS_LOGIN;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
    }

    private void signIn() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            sendGoogleLogin(account);
        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
//                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String name = ""+account.getDisplayName();
            String first = ""+account.getDisplayName();
            String last = ""+account.getDisplayName();
            String mail = ""+account.getEmail();
            String photoUrl = "" + account.getPhotoUrl();

            Log.d(TAG, "email" + mail);
            PreferenceStorage.saveSocialNetworkProfilePic(getApplicationContext(), photoUrl);

            String GCMKey = PreferenceStorage.getGCM(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(OSAConstants.PARAMS_USERNAME, name);
                jsonObject.put(OSAConstants.PARAMS_FIRST_NAME, first);
                jsonObject.put(OSAConstants.PARAMS_LAST_NAME, last);
                jsonObject.put(OSAConstants.PARAMS_GCM_KEY, GCMKey);
                jsonObject.put(OSAConstants.PARAMS_LOGIN_TYPE, "1");
                jsonObject.put(OSAConstants.PARAMS_MOBILE_TYPE, "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String serverURL = OSAConstants.BUILD_URL + OSAConstants.FB_GPLUS_LOGIN;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void facebookLogin() {

        loginManager = LoginManager.getInstance();
//        callbackManager = CallbackManager.Factory.create();

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (object != null) {
                            String name = object.optString("name");
                            String first = object.optString("first_name");
                            String last = object.optString("last_name");
                            String email = object.optString("email");
                            String fbUserID = object.optString("id");
                            String gender = object.optString("gender");
                            String birthday = object.optString("birthday");

                            Log.d(TAG, "email" + email + "facebook gender" + gender + "birthday" + birthday );

                            url = "https://graph.facebook.com/" + fbUserID + "/picture?type=large";
//                            disconnectFromFacebook();
                            // do action after Facebook login success
                            // or call your API
//                            PreferenceStorage.saveSocialNetworkProfilePic(getApplicationContext(), url);
                            whichService = "google";

                            String GCMKey = PreferenceStorage.getGCM(getApplicationContext());
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put(OSAConstants.PARAMS_USERNAME, name);
                                jsonObject.put(OSAConstants.PARAMS_FIRST_NAME, first);
                                jsonObject.put(OSAConstants.PARAMS_LAST_NAME, last);
                                jsonObject.put(OSAConstants.PARAMS_GCM_KEY, GCMKey);
                                jsonObject.put(OSAConstants.PARAMS_LOGIN_TYPE, "1");
                                jsonObject.put(OSAConstants.PARAMS_MOBILE_TYPE, "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                            String serverURL = OSAConstants.BUILD_URL + OSAConstants.FB_GPLUS_LOGIN;
                            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("LoginScreen", "---onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // here write code when get error
                Log.v("LoginScreen", "----onError: " + error.getMessage());
            }
        });
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/",
                null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // add this line
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            // Signed out, show unauthenticated UI.
//                tvDetails.setText("error occured..!");
//                updateUI(false);
            String okSet = "0";
            String newOk = okSet;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(OSAConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                    }
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//                    sharedPreferences.edit().clear().apply();
//                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestEmail()
//                            .build();
//                    // Build a GoogleSignInClient with the options specified by gso.
//                    LoginManager.getInstance().logOut();
//                    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//                    mGoogleSignInClient.signOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }


    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)){
            if (whichService.equalsIgnoreCase("mobile")) {
                Intent i = new Intent(getApplicationContext(), NumberVerificationActivity.class);
                startActivity(i);
            }
        }

    }

    @Override
    public void onError(String error) {
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}
