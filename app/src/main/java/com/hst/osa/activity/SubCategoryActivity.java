package com.hst.osa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hst.osa.R;

import com.hst.osa.adapter.BestSellingListAdapter;
import com.hst.osa.adapter.SubCategoryListAdapter;
import com.hst.osa.bean.support.Category;
import com.hst.osa.bean.support.CategoryList;
import com.hst.osa.bean.support.Product;
import com.hst.osa.bean.support.ProductList;
import com.hst.osa.bean.support.SubCategory;
import com.hst.osa.bean.support.SubCategoryList;
import com.hst.osa.fragment.BestSellingFragment;
import com.hst.osa.helpers.AlertDialogHelper;
import com.hst.osa.helpers.ProgressDialogHelper;
import com.hst.osa.servicehelpers.ServiceHelper;
import com.hst.osa.serviceinterfaces.IServiceListener;
import com.hst.osa.utils.OSAConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.util.Log.d;

public class SubCategoryActivity extends AppCompatActivity implements IServiceListener, SubCategoryListAdapter.OnItemClickListener, BestSellingListAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    private ArrayList<SubCategory> subCategoryArrayList = new ArrayList<>();
    private SubCategoryListAdapter mAdapter;

    private ArrayList<Product> productArrayList = new ArrayList<>();
    ProductList productList;

    private RecyclerView recyclerViewPopularProduct;
    private RecyclerView recyclerViewSubCategory;

    private String catId;
    private String subCatId;
    private String serviceCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        recyclerViewSubCategory = (RecyclerView) findViewById(R.id.sub_cat);
        recyclerViewPopularProduct = (RecyclerView)findViewById(R.id.sub_product_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubCategory.setLayoutManager(mLayoutManager);
        recyclerViewSubCategory.setHasFixedSize(true);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

        progressDialogHelper = new ProgressDialogHelper(this);

        catId = getIntent().getStringExtra("cat");

        showSubCategory();
    }

    private void showSubCategory(){

        serviceCall = "sub_category";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(OSAConstants.KEY_CAT_ID, catId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverUrl = OSAConstants.BUILD_URL + OSAConstants.SUB_CATEGORY_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
    }

    private boolean validateSignInResponse(JSONObject response){

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

        if (validateSignInResponse(response)){
            try{
//                Gson gson = new Gson();
//
//                JSONArray categoryObjData = response.getJSONArray("sub_category_list");
//                if (categoryObjData.length() > 0) {
//                    Category[] categories = gson.fromJson(categoryObjData.toString(), SubCategoryList[].class);
////                    categoryList = gson.fromJson(categoryObjData.toString(), SubCategoryList.class);
//                    categoryArrayList.addAll(categoryList.getCategoryArrayList());
//                    mAdapter = new SubCategoryListAdapter(categoryArrayList, this);
//                    recyclerViewSubCategory.setAdapter(mAdapter);
//                }
                if (serviceCall.equalsIgnoreCase("sub_category")) {

                    JSONArray subCategoryArray = response.getJSONArray("sub_category_list");
                    JSONObject subCatObj = subCategoryArray.getJSONObject(0);

                    Log.d(TAG, subCatObj.toString());

                    String id = "";
                    String txtSubCat = "";
                    subCategoryArrayList = new ArrayList<>();
                    subCategoryArrayList.add(new SubCategory("0", "ALL"));

                    for (int i = 0; i < subCategoryArray.length(); i++) {

                        id = subCategoryArray.getJSONObject(i).getString("id");
                        txtSubCat = subCategoryArray.getJSONObject(i).getString("category_name");
                        subCategoryArrayList.add(new SubCategory(id, txtSubCat));
                    }
                    mAdapter = new SubCategoryListAdapter(subCategoryArrayList, this);
                    recyclerViewSubCategory.setAdapter(mAdapter);
                }
                if (serviceCall.equalsIgnoreCase("sub_cat_product_list")){

                    Gson gson = new Gson();

                    JSONObject popularObjData = response.getJSONObject("popular_product_list");
                    productList = gson.fromJson(popularObjData.toString(), ProductList.class);
                    productArrayList.addAll(productList.getProductArrayList());
                    BestSellingListAdapter adasd = new BestSellingListAdapter(productArrayList, this);
                    GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
                    mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (adasd.getItemViewType(position) > 0) {
                                return adasd.getItemViewType(position);
                            } else {
                                return 1;
                            }
                            //return 2;
                        }
                    });
                    recyclerViewPopularProduct.setLayoutManager(mLayoutManager);
                    recyclerViewPopularProduct.setAdapter(adasd);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemClick(View view, int position) {
        d(TAG, "selected Category" + position);
        SubCategory category = null;
        if (mAdapter != null) {
            d(TAG, "while clicking");
            int actualIndex = mAdapter.getItemViewType(position);
            d(TAG, "actual index" + actualIndex);
            category = subCategoryArrayList.get(position);
        } else {
            category = subCategoryArrayList.get(position);
        }
        subCatId = category.getId();
        subCatProductList();
    }

    private void subCatProductList(){

        serviceCall = "sub_cat_product_list";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(OSAConstants.KEY_USER_ID, "");
            jsonObject.put(OSAConstants.KEY_CAT_ID, catId);
            jsonObject.put(OSAConstants.KEY_SUB_CAT, subCatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverUrl = OSAConstants.BUILD_URL + OSAConstants.PRODUCT_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
    }

    @Override
    public void onItemClickBestSelling(View view, int position) {

    }
}