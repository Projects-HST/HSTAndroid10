package com.hst.osa.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hst.osa.R;
import com.hst.osa.adapter.CategoryListAdapter;
import com.hst.osa.bean.support.CategoryList;
import com.hst.osa.customview.AViewFlipper;
import com.hst.osa.fragment.BestSellingFragment;
import com.hst.osa.helpers.AlertDialogHelper;
import com.hst.osa.helpers.ProgressDialogHelper;
import com.hst.osa.interfaces.DialogClickListener;
import com.hst.osa.servicehelpers.ServiceHelper;
import com.hst.osa.serviceinterfaces.IServiceListener;
import com.hst.osa.utils.OSAConstants;
import com.hst.osa.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;

public class ProductDetailActivity extends AppCompatActivity implements IServiceListener, DialogClickListener {

    private static final String TAG = ProductDetailActivity.class.getName();
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String productID;
    AViewFlipper aViewFlipper;

    private LinearLayout dotsLayout;
    private TextView[] dots;
    private ArrayList<String> imgUrl = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productID = getIntent().getStringExtra("productObj");
        aViewFlipper = findViewById(R.id.view_flipper);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        initiateServices();
        getDashboardServices();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitvity = 100;
            if (imgUrl.size() >= 1) {
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    SwipeLeft();
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    SwipeRight();
                }
            }
            return true;
        }
    };

    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);


    private void SwipeLeft() {
        aViewFlipper.setInAnimation(this, R.anim.in_from_right);
        aViewFlipper.showNext();
    }

    private void SwipeRight() {
        aViewFlipper.setInAnimation(this, R.anim.in_from_left);
        aViewFlipper.showPrevious();
    }

    private void setImageInFlipr(String imgUrl) {
        ImageView image = new ImageView(this);
        Picasso.get().load(imgUrl).into(image);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        aViewFlipper.addView(image);
    }

    public void initiateServices() {

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(OSAConstants.PARAM_MESSAGE);
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(final JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                Gson gson = new Gson();
//                JSONArray images = response.getJSONArray("image_result");
//                if (imgUrl.size() >= 0) {
//                    for (int i = 0; i < images.length(); i++) {
//                        imgUrl.add(images.getJSONObject(i).getString("gallery_url"));
//                    }
//                    for (int a = 0; a < imgUrl.size(); a++) {
//                        setImageInFlipr(imgUrl.get(a));
//                    }
//                }
                JSONObject productDetails = response.getJSONObject("product_details").getJSONObject("product_details");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.d(TAG, "Error while sign In");
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

    private void getDashboardServices() {
        JSONObject jsonObject = new JSONObject();
        String id = PreferenceStorage.getUserId(this);
        try {
//            jsonObject.put(SkilExConstants.USER_MASTER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(OSAConstants.KEY_USER_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = OSAConstants.BUILD_URL + OSAConstants.DASHBOARD;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

}