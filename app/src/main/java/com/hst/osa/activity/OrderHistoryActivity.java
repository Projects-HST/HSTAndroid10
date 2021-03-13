package com.hst.osa.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hst.osa.R;
import com.hst.osa.adapter.ReviewOrderListAdapter;
import com.hst.osa.bean.support.CartItem;
import com.hst.osa.bean.support.CartOrderList;
import com.hst.osa.helpers.AlertDialogHelper;
import com.hst.osa.helpers.ProgressDialogHelper;
import com.hst.osa.interfaces.DialogClickListener;
import com.hst.osa.servicehelpers.ServiceHelper;
import com.hst.osa.serviceinterfaces.IServiceListener;
import com.hst.osa.utils.OSAConstants;
import com.hst.osa.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;

public class OrderHistoryActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {
    private static final String TAG = OrderHistoryActivity.class.getName();

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    private TextView delivered, transit;

    private ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
    CartOrderList cartItemList;
    private ReviewOrderListAdapter mAdapter;
    private RecyclerView recyclerViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initiateServices();
        delivered = (TextView) findViewById(R.id.delivered);
        delivered.setOnClickListener(this);
        transit = (TextView) findViewById(R.id.in_transit);
        transit.setOnClickListener(this);

        getOrderHistory("Delivered");
    }

    @Override
    public void onClick(View view) {
        if (view == delivered) {
            transit.setBackground(null);
            delivered.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_size_color));
            getOrderHistory("Delivered");
        } if (view == transit) {
            transit.setBackground(null);
            delivered.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_size_color));
            getOrderHistory("Transit");
        }
    }

    public void initiateServices() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
    }

    private void getOrderHistory(String orderType) {
        JSONObject jsonObject = new JSONObject();
        String id = PreferenceStorage.getUserId(this);
        try {
            jsonObject.put(OSAConstants.KEY_USER_ID, id);
            jsonObject.put(OSAConstants.KEY_STATUS, orderType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = OSAConstants.BUILD_URL + OSAConstants.ORDER_HISTORY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

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
                String count = response.getString("order_count");
                Gson gson = new Gson();

                cartItemList = gson.fromJson(response.toString(), CartOrderList.class);
                cartItemArrayList.addAll(cartItemList.getCartItemArrayList());
//                mAdapter = new ReviewOrderListAdapter(this, cartItemArrayList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerViewCategory.setLayoutManager(mLayoutManager);
                recyclerViewCategory.setAdapter(mAdapter);
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

}