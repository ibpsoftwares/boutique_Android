package com.kftsoftwares.boutique.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.ListViewAdapter;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.GET_CATEGORIES;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;


public class Category extends Fragment {

    private ListView mListView;
    private ArrayList<String> mCategoryNameList;
    private ArrayList<String> mCategoryNameId;
    private RelativeLayout mNoDataFound;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity)context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mListView = view.findViewById(R.id.listView);
        mCategoryNameList = new ArrayList<>();
        mCategoryNameId = new ArrayList<>();
        mNoDataFound = view.findViewById(R.id.no_data_image);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategories();
            }
        });
        getCategories();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        mContext.mCartView.setVisibility(View.VISIBLE);
        mContext.mHeaderText.setText("Category");

        if (mContext.mCartCount!=0)
        {
            mContext.mCartCountText.setVisibility(View.VISIBLE);

        }
    }


    //---------------------GET CATEGORIES FROM SERVER------------------------//
    private void getCategories() {



        String tag_string_req = "string_req";

        mContext.showProgressBar(true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_CATEGORIES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    if (mCategoryNameList != null) {
                        mCategoryNameList.clear();
                    }
                    if (mCategoryNameId != null) {
                        mCategoryNameId.clear();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message") != null && jsonObject.getString("message").equalsIgnoreCase("No Data Found")) {
                        mNoDataFound.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);

                    } else {

                        mNoDataFound.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mCategoryNameList.add(jsonObject1.getString("name"));
                        mCategoryNameId.add(jsonObject1.getString("id"));

                    }
                    ListViewAdapter listViewAdapter = new ListViewAdapter(mContext,mCategoryNameList,mCategoryNameId);

                    mListView.setAdapter(listViewAdapter);
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
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                mContext.showProgressBar(true);

            }
        }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);
                return params;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
