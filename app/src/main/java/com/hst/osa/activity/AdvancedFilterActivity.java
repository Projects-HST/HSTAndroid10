package com.hst.osa.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.hst.osa.R;
import com.hst.osa.adapter.OrderHistoryDetailListAdapter;
import com.hst.osa.adapter.QuestionListAdapter;
import com.hst.osa.bean.support.CartItem;
import com.hst.osa.bean.support.CartOrderList;
import com.hst.osa.bean.support.Question;
import com.hst.osa.bean.support.QuestionList;
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

public class AdvancedFilterActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, OrderHistoryDetailListAdapter.OnItemClickListener, QuestionListAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = ReplaceProductActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String resCheck = "";
    private String prodOrderID = "";
    private String questionID = "1";

    private TextView btnSubmit;

    private ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
    CartOrderList cartItemList;
    private OrderHistoryDetailListAdapter mAdapter;
    private RecyclerView recyclerViewCategory;

    private ArrayList<Question> questionArrayList = new ArrayList<>();
    QuestionList questionList;
    private QuestionListAdapter questionListAdapter;
    private RecyclerView recyclerViewQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewCategory = (RecyclerView) findViewById(R.id.listView_cart);
        recyclerViewQuestion = (RecyclerView) findViewById(R.id.listView_questions);

        btnSubmit = (TextView) findViewById(R.id.submit);
        btnSubmit.setOnClickListener(this);

        initiateServices();
        trackORder();
    }

    public void initiateServices() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
    }

    private void trackORder() {
        resCheck = "track";
        JSONObject jsonObject = new JSONObject();
        String id = PreferenceStorage.getUserId(this);
        String oid = PreferenceStorage.getOrderId(this);
        try {
            jsonObject.put(OSAConstants.KEY_USER_ID, id);
            jsonObject.put(OSAConstants.PARAMS_ORDER_ID, oid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = OSAConstants.BUILD_URL + OSAConstants.TRACK_ORDER;
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
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                if (resCheck.equalsIgnoreCase("track")) {
                    JSONArray orderObjData = response.getJSONArray("order_details");

                    JSONObject data = orderObjData.getJSONObject(0);

                }

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
    public void onItemClickCart(View view, int position) {

    }

    @Override
    public void onItemClickSize(View view, int position) {
        Question question = null;
        question = questionArrayList.get(position);
        questionID = question.getid();
        for (int i = 0; i < questionArrayList.size(); i++) {
            if (i == position) {
                questionArrayList.get(i).setstatus("check");
            } else {
                questionArrayList.get(i).setstatus("Active");
            }
        }
//        questionListAdapter.notifyAll();
        questionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
        }
    }
}
