package com.kftsoftwares.boutique.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

import static com.kftsoftwares.boutique.utils.Constants.GET_ALL_PRODUCTS;
import static com.kftsoftwares.boutique.utils.Constants.GET_BANNER_IMAGES;


public class Home extends Fragment implements View.OnClickListener {

    private ArrayList<GetAllProductModel> mGetAllProductModels;
    private GridView mGridView;
    private ArrayList<String> mBannerImages;
    private ViewPagerAdapter mViewPagerAdapter;
    private ImageView mRightImage ,mLeftImage;
    private ViewPager mViewPager;
    private int mCount;
    private int mVal=0;
    int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 1500; // time in milliseconds between successive task executions.


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getCartList();
        ((MainActivity)getActivity()).mCartView.setVisibility(View.VISIBLE);
        if (((MainActivity)getActivity()).mCartCount!=0)
        {
            ((MainActivity)getActivity()).mCartCountText.setVisibility(View.VISIBLE);

        }
        getAllProducts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = view.findViewById(R.id.viewPager);

        mViewPagerAdapter = new ViewPagerAdapter(getActivity(),new ArrayList<String>());
        mViewPager.setAdapter(mViewPagerAdapter);

        mGridView = view.findViewById(R.id.gridView);

        mRightImage = view.findViewById(R.id.rightArrow);
        mLeftImage = view.findViewById(R.id.leftArrow);

        mRightImage.setOnClickListener(this);
        mLeftImage.setOnClickListener(this);

        mGetAllProductModels = new ArrayList<>();
        mBannerImages = new ArrayList<>();
        getBannerImages();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position + 1 == mCount)
                {
                    mLeftImage.setVisibility(View.GONE);
                }
                else {
                    mLeftImage.setVisibility(View.VISIBLE);

                }
                if (position == 0)
                {
                    mRightImage.setVisibility(View.GONE);
                }
                else {
                    mRightImage.setVisibility(View.VISIBLE);

                }

                currentPage = position;
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
                if (currentPage == mCount) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
                mVal = currentPage;
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        return view;

    }


    //---------------GET ALL PRODUCTS---------------------------//

    public void getAllProducts() {

        if (mGetAllProductModels != null) {
            mGetAllProductModels.clear();
        }

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                GET_ALL_PRODUCTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data1");
                    GetAllProductModel getAllProductModel = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        getAllProductModel = new GetAllProductModel();
                        getAllProductModel.setCategoryId(jsonObject1.getString("category_id"));
                        getAllProductModel.setTitle(jsonObject1.getString("title"));
                        getAllProductModel.setPrice(jsonObject1.getString("original_price"));
                        getAllProductModel.setOfferPrice(jsonObject1.getString("offer_price"));
                        getAllProductModel.setId(jsonObject1.getString("id"));
                        if (jsonObject1.has("Wishlist"))
                        {
                            getAllProductModel.setWish_list(jsonObject1.getString("Wishlist"));

                        }
                        else {
                            getAllProductModel.setWish_list("0");

                        }
                          getAllProductModel.setBrandName(jsonObject1.getString("brand"));
                        getAllProductModel.setCategoryName(jsonObject1.getString("category_name"));
                        getAllProductModel.setImage1(jsonObject1.getString("image1"));

                        mGetAllProductModels.add(getAllProductModel);

                    }


                    GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), mGetAllProductModels);
                    mGridView.setAdapter(gridViewAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        );

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void getBannerImages() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                GET_BANNER_IMAGES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("bannerImages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        mBannerImages.add(jsonObject1.getString("image"));

                    }
                    mCount = jsonArray.length();
                    mViewPagerAdapter.update(mBannerImages);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        );

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftArrow:
                if (mViewPager.getCurrentItem() == mCount)
                {

                }
                else {
                    mVal++;
                    mViewPager.setCurrentItem(mVal);
                }

                break;
            case R.id. rightArrow:
                if (mViewPager.getCurrentItem() != 0)
                {
                    mVal--;
                    mViewPager.setCurrentItem(mVal);
                }
                break;
        }

    }
}
