package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.GridViewAdapter;
import com.kftsoftwares.boutique.Adapter.ViewPagerAdapter;
import com.kftsoftwares.boutique.Models.GetAllProductModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.kftsoftwares.boutique.utils.Constants.GET_ALL_PRODUCTS;
import static com.kftsoftwares.boutique.utils.Constants.GET_BANNER_IMAGES;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;


public class Home extends Fragment implements View.OnClickListener {

    private ArrayList<GetAllProductModel> mGetAllProductModels;
    private GridView mGridView;
    private ArrayList<String> mBannerImages;
    private ArrayList<String> mNewBannerImages;
    private ViewPagerAdapter mViewPagerAdapter;
    private ImageView mRightImage, mLeftImage;
    private ViewPager mViewPager;
    private int mCount;
    private int mVal = 0;
    int currentPage = 0;
    private SharedPreferences sharedPreferences;
    private Timer timer;
    final long DELAY_MS = 800;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 1800; // time in milliseconds between successive task executions.
    private int n = 0;
    private int mTotalsize = 0;
    int mMaxVal = 360;
    private MainActivity mContext;
    private TextView mNoDataFound;

    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity)context);
    }



    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
             mContext.getLocalWishListData();
          mContext.getLocalCartListData();
        } else {
            mContext.getCartList();
        }

       mContext.mCartView.setVisibility(View.VISIBLE);
         mContext.mHeaderText.setText("Collections");
        if ( mContext.mCartCount != 0) {
             mContext.mCartCountText.setVisibility(View.VISIBLE);

        }
      getAllProducts();

//        if (mContext.mGetAllProductModels.size()>0) {
//            mNoDataFound.setVisibility(View.GONE);
//            mGridView.setVisibility(View.VISIBLE);
//            GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mContext.mGetAllProductModels);
//            mGridView.setAdapter(gridViewAdapter);
//        }
//        else {
//            mGridView.setVisibility(View.GONE);
//            mNoDataFound.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_View);

        mViewPager = view.findViewById(R.id.viewPager);

        mViewPagerAdapter = new ViewPagerAdapter(mContext, new ArrayList<String>());
        mViewPager.setAdapter(mViewPagerAdapter);

        mGridView = view.findViewById(R.id.gridView);

        mRightImage = view.findViewById(R.id.rightArrow);
        mLeftImage = view.findViewById(R.id.leftArrow);
mNoDataFound = view.findViewById(R.id.noDataFound);
        mRightImage.setOnClickListener(this);
        mLeftImage.setOnClickListener(this);

        mGetAllProductModels = new ArrayList<>();
        mBannerImages = new ArrayList<>();
        mNewBannerImages = new ArrayList<>();
        getBannerImages();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == mMaxVal) {
                    mLeftImage.setVisibility(View.GONE);
                } else {
                    mLeftImage.setVisibility(View.VISIBLE);

                }
                if (position == 0) {
                    mRightImage.setVisibility(View.GONE);
                } else {
                    mRightImage.setVisibility(View.VISIBLE);

                }

                currentPage = position;
                mVal = currentPage;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == mMaxVal) {
                    currentPage = 0;
                }


                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {


                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        sharedPreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return view;

    }


    private void getArray(ArrayList<String> banner, int val) {
        if (val == banner.size()) {
            n = 0;
        }

        if(mTotalsize <=mMaxVal)

        {
            mTotalsize++;
            if (n < banner.size()) {
                mNewBannerImages.add(banner.get(n));
                n++;
                getArray(mBannerImages, n);

            }
        }
        else {

            mViewPagerAdapter.update(mNewBannerImages);


        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftArrow:
                if (mViewPager.getCurrentItem() == mMaxVal) {
                } else {
                    mVal++;
                    mViewPager.setCurrentItem(mVal);
                }
                break;
            case R.id.rightArrow:
                if (mViewPager.getCurrentItem() != 0) {
                    mVal--;
                    mViewPager.setCurrentItem(mVal);
                }
                break;
        }

    }



    //---------------GET ALL PRODUCTS---------------------------//
    public void getAllProducts() {

        if (mGetAllProductModels != null) {
            mGetAllProductModels.clear();
        }

        String tag_string_req = "string_req";


        mContext.showProgressBar(true);
        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_ALL_PRODUCTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data1");
                    GetAllProductModel getAllProductModel = null;

                    if (jsonArray.length()>0) {
                        mNoDataFound.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            getAllProductModel = new GetAllProductModel();
                            getAllProductModel.setCategoryId(jsonObject1.getString("category_id"));
                            getAllProductModel.setTitle(jsonObject1.getString("title"));
                            getAllProductModel.setPrice(jsonObject1.getString("original_price"));
                            getAllProductModel.setOfferPrice(jsonObject1.getString("offer_price"));
                            getAllProductModel.setId(jsonObject1.getString("id"));
                            if (jsonObject1.has("wishlist")) {
                                if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("") && mContext.mUserIdArrayList.contains(jsonObject1.getString("id"))) {
                                    getAllProductModel.setWish_list("1");
                                } else {
                                    getAllProductModel.setWish_list(jsonObject1.getString("wishlist"));
                                }
                            } else {
                                getAllProductModel.setWish_list("0");

                            }

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Symbol,jsonObject1.getString("symbol"));
                            editor.commit();
                            editor.apply();

                            getAllProductModel.setBrandName(jsonObject1.getString("brand"));
                            getAllProductModel.setCategoryName(jsonObject1.getString("category_name"));
                            getAllProductModel.setImage1(jsonObject1.getString("image1"));

                            mGetAllProductModels.add(getAllProductModel);

                        }


                        GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mGetAllProductModels);
                        mGridView.setAdapter(gridViewAdapter);
                    }
                    else {

                        mGridView.setVisibility(View.GONE);
                        mNoDataFound.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    mContext.showProgressBar(false);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mContext.showProgressBar(false);
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                return params;

            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req,mContext);


    }



    //-------------GET BANNER IMAGES----------------------//
    private void getBannerImages() {

        String tag_string_req = "string_req";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                GET_BANNER_IMAGES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                /*    JSONArray jsonArray = jsonObject.getJSONArray("bannerImages");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        mBannerImages.add(jsonObject1.getString("image"));

                    }


                    mCount = jsonArray.length();
                    getArray(mBannerImages,0);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);
                return params;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req, mContext);


    }


}
