package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.AlbumsAdapter;
import com.kftsoftwares.boutique.Models.Banner;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.Models.GetAllProductModel;
import com.kftsoftwares.boutique.Models.Size;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.utils.SpacesItemDecoration;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.GET_ALL_PRODUCTS;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;


public class HomeNew extends Fragment {
    private MainActivity mNavigationActivity;
    private int mPageSize = 1;
    private boolean mLoadMore = false;
    private boolean mLoading = false;
    private AlbumsAdapter recyclerAdapter;

    private ArrayList<GetAllProductModel> mGetAllProductModels;
    private DatabaseHandler mDatabaseHandler;

    private SharedPreferences sharedPreferences;
    private RecyclerView mRecyclerView;
    // private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNavigationActivity = ((MainActivity) context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
            mNavigationActivity.getLocalWishListData();
            mNavigationActivity.getLocalCartListData();
        } else {
            mNavigationActivity.getCartList();
        }

        mNavigationActivity.mCartView.setVisibility(View.VISIBLE);
        mNavigationActivity.mHeaderText.setText("");
        if (mNavigationActivity.mCartCount != 0) {
            mNavigationActivity.mCartCountText.setVisibility(View.VISIBLE);

        }
        recyclerAdapter = new AlbumsAdapter(mNavigationActivity, new ArrayList<GetAllProductModel>());
        mRecyclerView.setAdapter(recyclerAdapter);
        // getBannerImages(false);
        getAllProducts(false);

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_demo, container, false);

        Bundle bundle = new Bundle();
        mGetAllProductModels = new ArrayList<>();
        sharedPreferences = mNavigationActivity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mRecyclerView = view.findViewById(R.id.recycler_View);

        mDatabaseHandler = new DatabaseHandler(mNavigationActivity);

        // mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);

        final GridLayoutManager mLayoutManager =
                new GridLayoutManager(mNavigationActivity, 2);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else if (position == 7) {
                    return 2;
                } else
                    return 1;
            }
        });

     /*   mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBannerImages(true);
            }
        });*/

        mRecyclerView.setLayoutManager(mLayoutManager);

        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(2);

        mRecyclerView.addItemDecoration(spacesItemDecoration);




       /* if (mNavigationActivity.mGetAllProductModels.size() > 0) {
            recyclerAdapter.updateData(mNavigationActivity.mGetAllProductModels);

        } else {

        }
*/
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (mLoadMore && !mLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= mPageSize) {
                        mLoading = true;
                    }
                }
            }
        });


        return view;
    }


    public void getAllProducts(boolean value) {

        if (mGetAllProductModels != null) {
            mGetAllProductModels.clear();
        }

        if (mNavigationActivity.mOffer1 != null) {
            mNavigationActivity.mOffer1.clear();
        }
        if (mNavigationActivity.mOffer2 != null) {
            mNavigationActivity.mOffer2.clear();
        }

        if (!value)

        {
            mNavigationActivity.showProgressBar(true);
        }
        mNavigationActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        String tag_string_req = "string_req";


        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_ALL_PRODUCTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                        mNavigationActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        mNavigationActivity.showProgressBar(false);
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("data1");

                        JSONObject jsonObjectBanners = jsonObject.getJSONObject("banners");
                        mNavigationActivity.mBannerImage = jsonObjectBanners.getString("bannerImages");

                        // getAllProducts();
                        JSONArray jsonArray2 = jsonObjectBanners.getJSONArray("offer1");

                        Banner banner1 = null;
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                            banner1 = new Banner();
                            banner1.setId(jsonObject2.getString("id"));
                            banner1.setImage(jsonObject2.getString("image"));
                            mNavigationActivity.mOffer1.add(banner1);

                        }
                        JSONArray jsonArray3 = jsonObjectBanners.getJSONArray("offer2");

                        Banner banner2 = null;
                        for (int i = 0; i < jsonArray3.length(); i++) {
                            JSONObject jsonObject3 = jsonArray3.getJSONObject(i);
                            banner2 = new Banner();
                            banner2.setId(jsonObject3.getString("id"));
                            banner2.setImage(jsonObject3.getString("image"));
                            mNavigationActivity.mOffer2.add(banner2);
                        }


                        GetAllProductModel getAllProductModel = null;

                        if (jsonArray.length() > 0) {
                            // mNoDataFound.setVisibility(View.GONE);
                            //  mGridView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                getAllProductModel = new GetAllProductModel();
                                getAllProductModel.setCategoryId(jsonObject1.getString("category_id"));
                                getAllProductModel.setTitle(jsonObject1.getString("title"));
                                getAllProductModel.setId(jsonObject1.getString("id"));
                                getAllProductModel.setPrice(jsonObject1.getString("original_price"));
                                getAllProductModel.setOfferPrice(jsonObject1.getString("offer_price"));
                                getAllProductModel.setId(jsonObject1.getString("id"));
                                getAllProductModel.setStock_size(jsonObject1.getString("total_stock"));

                                JSONArray sizeJsonArray = jsonObject1.getJSONArray("size");
                                Size size =null;

                                ArrayList<Size> sizes = new ArrayList<>();
                                sizes.clear();
                                for (int s = 0; s < sizeJsonArray.length(); s++) {
                                    JSONObject jsonObject_size = sizeJsonArray.getJSONObject(s);
                                    size = new Size();
                                    size.setSize(jsonObject_size.getString("size"));
                                    size.setStock(jsonObject_size.getString("stock"));
                                    size.setSize_id(jsonObject_size.getString("id"));
                                    sizes.add(size);
                                }
                                getAllProductModel.setSizeArrayList(sizes);


                                if (jsonObject1.has("wishlist")) {
                                    if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("") && mNavigationActivity.mUserIdArrayList.contains(jsonObject1.getString("id"))) {
                                        getAllProductModel.setWish_list("1");
                                    } else {
                                        getAllProductModel.setWish_list(jsonObject1.getString("wishlist"));
                                    }
                                } else {
                                    getAllProductModel.setWish_list("0");

                                }
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Symbol, jsonObject1.getString("symbol"));
                                editor.apply();

                                getAllProductModel.setBrandName(jsonObject1.getString("brand"));
                                getAllProductModel.setCategoryName(jsonObject1.getString("category_name"));
                                getAllProductModel.setImage1(jsonObject1.getString("image1"));

                                mGetAllProductModels.add(getAllProductModel);

                            }


                            recyclerAdapter.updateData(mGetAllProductModels);

                            if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
                                updateDataOnLocal(mGetAllProductModels);
                            }
                            mNavigationActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            mNavigationActivity.showProgressBar(false);


                        } else {
                            mNavigationActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            mNavigationActivity.showProgressBar(false);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    //      mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mNavigationActivity.showProgressBar(false);
                Toast.makeText(mNavigationActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req, mNavigationActivity);

    }

    private void updateDataOnLocal(ArrayList<GetAllProductModel> mGetAllProductModels) {

        List<CartViewModel> wishlist = mDatabaseHandler.getAllDataOfWishlist();
        List<CartViewModel> cartlist = mDatabaseHandler.getAllDataOfCart();


        //--------For WishList---------------//
        for (int i = 0; i < mGetAllProductModels.size(); i++) {

            for (int j = 0 ;j<wishlist.size();j++)
            {
                if (mGetAllProductModels.get(i).getId().equalsIgnoreCase(wishlist.get(j).getProduct_id()))
                {
                    mDatabaseHandler.updateWishListStock(mGetAllProductModels.get(i).getId(),mGetAllProductModels.get(i).getStock_size());

                }

            }


        }

        //--------For Cart---------------//

        //check The total Array From server
        for (int i = 0; i < mGetAllProductModels.size(); i++) {


            //check The local count Array

            for (int j = 0 ;j<cartlist.size();j++)
            {

                // compare the same id
                if (mGetAllProductModels.get(i).getId().equalsIgnoreCase(cartlist.get(j).getProduct_id()))
                {

                    // check the count of single product in cart with different sizes

                        ArrayList<Size> sizes = mGetAllProductModels.get(i).getSizeArrayList();

                        String localsize = cartlist.get(j).getSize();

                        for (int k = 0; k < sizes.size(); k++) {
                            if (sizes.get(k).getSize().equalsIgnoreCase(localsize)) {
                                mDatabaseHandler.updateCartStock(mGetAllProductModels.get(i).getId(), sizes.get(k).getStock(), cartlist.get(j).getSize());

                            }

                        }


                }

            }


        }
        List<CartViewModel> wishlist2 = mDatabaseHandler.getAllDataOfWishlist();
        List<CartViewModel> cartlist2 = mDatabaseHandler.getAllDataOfCart();

    }


}
