package com.hst.osa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hst.osa.R;
import com.hst.osa.adapter.BestSellingListAdapter;
import com.hst.osa.adapter.CategoryHorizontalListAdapter;
import com.hst.osa.adapter.CategoryListAdapter;
import com.hst.osa.bean.support.Category;
import com.hst.osa.bean.support.CategoryList;
import com.hst.osa.bean.support.Product;
import com.hst.osa.bean.support.ProductList;
import com.hst.osa.fragment.BestSellingFragment;
import com.hst.osa.helpers.AlertDialogHelper;
import com.hst.osa.helpers.ProgressDialogHelper;
import com.hst.osa.interfaces.DialogClickListener;
import com.hst.osa.servicehelpers.ServiceHelper;
import com.hst.osa.serviceinterfaces.IServiceListener;
import com.hst.osa.utils.OSAConstants;
import com.hst.osa.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;

public class CategoryActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, CategoryListAdapter.OnItemClickListener{

    private static final String TAG = BestSellingFragment.class.getName();
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private Handler mHandler = new Handler();
    int totalCount = 0, checkrun = 0;
    protected boolean isLoadingForFirstTime = true;

    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    Category category;
    CategoryList categoryList;
    private CategoryListAdapter mAdapter;
    private RecyclerView recyclerViewCategory;
    private View rootView;
    private TextView itemCount;

    public static BestSellingFragment newInstance(int position) {
        BestSellingFragment frag = new BestSellingFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewCategory = (RecyclerView) findViewById(R.id.listView_best_selling);
        itemCount = (TextView) findViewById(R.id.item_count);
        initiateServices();
        getDashboardServices();
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

                JSONObject categoryObjData = response.getJSONObject("cat_list");
                categoryList = gson.fromJson(categoryObjData.toString(), CategoryList.class);
                categoryArrayList.addAll(categoryList.getCategoryArrayList());
                mAdapter = new CategoryListAdapter(categoryArrayList, this);
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
                mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (mAdapter.getItemViewType(position) > 0) {
                            return mAdapter.getItemViewType(position);
                        } else {
                            return 1;
                        }
                        //return 2;
                    }
                });
                recyclerViewCategory.setLayoutManager(mLayoutManager);
                recyclerViewCategory.setAdapter(mAdapter);
//                itemCount.setText(categoryArrayList.size() + " Items");
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

    @Override
    public void onItemClickCategory(View view, int position) {
        d(TAG, "onEvent list item click" + position);
        Category category = null;
        if ((mAdapter != null)) {
            d(TAG, "while searching");
            int actualindex = mAdapter.getItemViewType(position);
            d(TAG, "actual index" + actualindex);
            category = categoryArrayList.get(actualindex);
        } else {
            category = categoryArrayList.get(position);
        }
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra("cat", category.getid());
        startActivity(intent);

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
