package com.hst.osa.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hst.osa.R;
import com.hst.osa.adapter.AdvertisementListAdapter;
import com.hst.osa.adapter.CategoryHorizontalListAdapter;
import com.hst.osa.bean.support.Advertisement;
import com.hst.osa.bean.support.AdvertisementList;
import com.hst.osa.bean.support.Category;
import com.hst.osa.bean.support.CategoryList;
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


public class DashboardFragment extends Fragment implements IServiceListener, DialogClickListener, CategoryHorizontalListAdapter.OnItemClickListener, AdvertisementListAdapter.OnItemClickListener {

    private static final String TAG = DashboardFragment.class.getName();
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private Handler mHandler = new Handler();
    int totalCount = 0, checkrun = 0;
    protected boolean isLoadingForFirstTime = true;

    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    Category category;
    CategoryList categoryList;
    private CategoryHorizontalListAdapter mAdapter;
    private RecyclerView recyclerViewCategory;

    private ArrayList<Advertisement> advertisementArrayList = new ArrayList<>();
    Advertisement advertisement;
    AdvertisementList advertisementList;
    private RecyclerView recyclerViewAdvertisement;

    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    private View rootView;
    private ViewFlipper viewFlipper;
    private String res = "";
    private ArrayList<String> imgUrl = new ArrayList<>();
    private String id = "";
    private Intent intent;
    private LinearLayout layout_all;


    public static DashboardFragment newInstance(int position) {
        DashboardFragment frag = new DashboardFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initiateServices();

        recyclerViewCategory = (RecyclerView) rootView.findViewById(R.id.listView_categories);
        recyclerViewAdvertisement = (RecyclerView) rootView.findViewById(R.id.listView_ads);
//        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.listView_trends);
//        layout_all = (LinearLayout) rootView.findViewById(R.id.layout_all);

//      create animations
        slide_in_left = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_left);
        slide_in_right = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_right);
        slide_out_left = AnimationUtils.loadAnimation(getActivity(), R.anim.out_to_left);
        slide_out_right = AnimationUtils.loadAnimation(getActivity(), R.anim.out_to_right);

        viewFlipper = rootView.findViewById(R.id.view_flipper);


        viewFlipper.setInAnimation(slide_in_right);
        //set the animation for the view leaving th screen
        viewFlipper.setOutAnimation(slide_out_left);
//        loadMoreListView = (ListView) rootView.findViewById(R.id.list_main_category);
//        loadMoreListView.setOnItemClickListener(this);
//        PreferenceStorage.saveServiceCount(getActivity(), "");
//        PreferenceStorage.saveRate(getActivity(), "");
        getDashboardServices();
//        loadMob();
        return rootView;
    }

    public void initiateServices() {

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

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
                        AlertDialogHelper.showSimpleAlertDialog(rootView.getContext(), msg);

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
                if (res.equalsIgnoreCase("dashboard")) {
                    JSONObject bannerObjData = response.getJSONObject("banner_list");
                    JSONArray imgdata = bannerObjData.getJSONArray("data");
                    for (int i = 0; i < imgdata.length(); i++) {
                        imgUrl.add(imgdata.getJSONObject(i).getString("banner_image"));
                    }
                    for (int i = 0; i < imgUrl.size(); i++) {
                        // create dynamic image view and add them to ViewFlipper
                        setImageInFlipr(imgUrl.get(i));
                    }
                    Gson gson = new Gson();

                    JSONObject categoryObjData = response.getJSONObject("cat_list");
                    categoryList = gson.fromJson(categoryObjData.toString(), CategoryList.class);
                    categoryArrayList.addAll(categoryList.getCategoryArrayList());
                    mAdapter = new CategoryHorizontalListAdapter(categoryArrayList, this);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewCategory.setLayoutManager(layoutManager);
//                    mRecyclerView.setLayoutManager(mLayoutManager);
                    recyclerViewCategory.setAdapter(mAdapter);

                    JSONObject advertisementObjData = response.getJSONObject("ads_list");
                    advertisementList = gson.fromJson(advertisementObjData.toString(), AdvertisementList.class);
                    advertisementArrayList.addAll(advertisementList.getAdvertisementArrayList());
                    AdvertisementListAdapter advertisementListAdapter = new AdvertisementListAdapter(advertisementArrayList, this);
                    RecyclerView.LayoutManager mLayoutManagerAds = new LinearLayoutManager(getActivity());
                    recyclerViewAdvertisement.setLayoutManager(mLayoutManagerAds);
                    recyclerViewAdvertisement.setAdapter(advertisementListAdapter);

                } else if (res.equalsIgnoreCase("trend")) {

//                    Gson gson = new Gson();
//                    trendingServicesArrayList = gson.fromJson(response.toString(), TrendingServicesList.class);
//                    if (trendingServicesArrayList.getserviceArrayList() != null && trendingServicesArrayList.getserviceArrayList().size() > 0) {
//                        int totalCount = trendingServicesArrayList.getCount();
////                    this.serviceHistoryArrayList.addAll(ongoingServiceList.getserviceArrayList());
//                        boolean isLoadingForFirstTime = false;
////                        updateListAdapter(serviceHistoryList.getFeedbackArrayList());
//                        loadMembersList(trendingServicesArrayList.getserviceArrayList().size());
//                    } else {
//                        if (trendingArrayList != null) {
//                            trendingArrayList.clear();
////                            updateListAdapter(serviceHistoryList.getFeedbackArrayList());
//                            loadMembersList(trendingServicesArrayList.getserviceArrayList().size());
//                        }
//                    }
//                    loadMob();
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
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onItemClick(View view, int position) {
//        d(TAG, "onEvent list item click" + position);
//        Category category = null;
//        if ((categoryListAdapter != null) && (categoryListAdapter.ismSearching())) {
//            d(TAG, "while searching");
//            int actualindex = categoryListAdapter.getActualEventPos(position);
//            d(TAG, "actual index" + actualindex);
//            category = categoryArrayList.get(actualindex);
//        } else {
//            category = categoryArrayList.get(position);
//        }
//        intent = new Intent(getActivity(), SubCategoryActivity.class);
//        intent.putExtra("cat", category);
//        startActivity(intent);

    }

    private void setImageInFlipr(String imgUrl) {
        ImageView image = new ImageView(rootView.getContext());
        Picasso.get().load(imgUrl).into(image);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        viewFlipper.addView(image);
    }

    private void getDashboardServices() {
        res = "dashboard";
        JSONObject jsonObject = new JSONObject();
        id = PreferenceStorage.getUserId(getActivity());
        try {
//            jsonObject.put(SkilExConstants.USER_MASTER_ID, PreferenceStorage.getUserId(getActivity()));
            jsonObject.put(OSAConstants.KEY_USER_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = OSAConstants.BUILD_URL + OSAConstants.DASHBOARD;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }
}
