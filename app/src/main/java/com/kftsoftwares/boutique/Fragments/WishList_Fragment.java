package com.kftsoftwares.boutique.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.kftsoftwares.boutique.Adapter.WishListAdapter;
import com.kftsoftwares.boutique.Interface.WishListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.GET_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.MOVETOWISHLIST;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.REMOVE_FROM_WISHLIST;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class WishList_Fragment extends Fragment implements WishListInterface {

    private ListView mListview;
    private ArrayList<CartViewModel> mCartViewModels;
    private RelativeLayout mWishListRelativeLayout;
    private DatabaseHandler mDatabaseHandler;
    private ArrayList<String> mClothIds;

    private SharedPreferences sharedPreferences;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).mCartView.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).mHeaderText.setText("Wishlist");

        if (((MainActivity) getActivity()).mCartCount != 0) {
            ((MainActivity) getActivity()).mCartCountText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list_, container, false);
        mListview = view.findViewById(R.id.listView);
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mWishListRelativeLayout = view.findViewById(R.id.no_data_image);
        mCartViewModels = new ArrayList<>();
        mClothIds = new ArrayList<>();
        if (mClothIds!=null)
        {
            mClothIds.clear();
        }

        mDatabaseHandler = new DatabaseHandler(getActivity());
        if (sharedPreferences.getString(User_ID,"").equalsIgnoreCase("")) {
           getLocalWishListData();
        }
        else {
          getWishList();

        }


        String str = TextUtils.join(",", mClothIds);;
        testingFromWishList(str);
        return view;
    }

    private void getLocalWishListData() {

        if (mCartViewModels != null) {
            mCartViewModels.clear();
        }

        mCartViewModels.addAll(mDatabaseHandler.getAllDataOfWishlist());

        if (mCartViewModels.size()>0)
        {
            mWishListRelativeLayout.setVisibility(View.GONE);
            mListview.setVisibility(View.VISIBLE);

            for (int i = 0;i<mCartViewModels.size();i++)
            {
                mClothIds.add(mCartViewModels.get(i).getClothId());
            }

        }
        else {
            mWishListRelativeLayout.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.GONE);

        }
        WishListAdapter wishListAdapter = new WishListAdapter(getActivity(), mCartViewModels, WishList_Fragment.this);

        mListview.setAdapter(wishListAdapter);
    }



    //----------------GET WISHLIST DATA FROM SERVER-----------//
    private void getWishList() {
        String tag_string_req = "string_req";
        if (mCartViewModels != null) {
            mCartViewModels.clear();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

       final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_WISH_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("message") && jsonObject.getString("message") != null && jsonObject.getString("message").equalsIgnoreCase("Wishlist Empty")) {

                        mWishListRelativeLayout.setVisibility(View.VISIBLE);
                        mListview.setVisibility(View.GONE);
                    } else {
                        mWishListRelativeLayout.setVisibility(View.GONE);
                        mListview.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("Wishlist");

                        CartViewModel cartViewModel = null;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            cartViewModel = new CartViewModel();
                            cartViewModel.setCategoryId(object.getString("category_id"));
                            cartViewModel.setClothId(object.getString("cloth_id"));
                            cartViewModel.setImage1(object.getString("image1"));
                            cartViewModel.setDescription(object.getString("description"));
                            cartViewModel.setTitle(object.getString("title"));
                            cartViewModel.setPrice(object.getString("original_price"));
                            cartViewModel.setOfferPrice(object.getString("offer_price"));
                            cartViewModel.setBrand(object.getString("brand"));

                            mCartViewModels.add(cartViewModel);
                        }

                        WishListAdapter wishListAdapter = new WishListAdapter(getActivity(), mCartViewModels, WishList_Fragment.this);

                        mListview.setAdapter(wishListAdapter);
                    }
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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userId);
                return params;

            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void updateWishList(String value) {
        if (sharedPreferences.getString(User_ID,"").equalsIgnoreCase(""))
        {
            mDatabaseHandler.removeDataFromWishList(value);
            getLocalWishListData();
            ((MainActivity)getActivity()).getLocalWishListData();
        }
        else
        {
            deleteFromWishList(value);
        }
    }

    @Override
    public void moveToWishList(String clothId) {
        if (sharedPreferences.getString(User_ID,"").equalsIgnoreCase(""))
        {

            if (mDatabaseHandler.CheckIsDataAlreadyInDBorNot(clothId))
            {
                Toast.makeText(getActivity(), "Already Exists In Cart", Toast.LENGTH_SHORT).show();
            }
            else {
                mDatabaseHandler.updateCategory("cart",clothId);
                getLocalWishListData();
                ((MainActivity)getActivity()).getLocalCartListData();
                ((MainActivity)getActivity()).getLocalWishListData();
            }
        }   else
        {
            moveToWishListMehtod(clothId);
        }
    }

    //--------------DELETE DATA FROM WISH LIST--------------------------------//
    private void deleteFromWishList(final String cloth_id) {
        String tag_string_req = "string_req";
        if (mCartViewModels != null) {
            mCartViewModels.clear();
        }
        final String userId = sharedPreferences.getString(User_ID, "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_WISHLIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    getWishList();
                    ((MainActivity) getActivity()).getWishList();
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
        )

        {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("user_id", userId);
                map.put("cloth_id", cloth_id);
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    //--------------TESTING WISH LIST--------------------------------//
    private void testingFromWishList(final String cloth_id ) {
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://kftsoftwares.com/ecom/recipes/sendmewishlistdatafortesting", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    getWishList();
                    ((MainActivity) getActivity()).getWishList();
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
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("user_id", "47");
                map.put("cloth_id", cloth_id);
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    //--------------Move To  WISH LIST--------------------------------//
    private void moveToWishListMehtod( final String clothId) {
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                MOVETOWISHLIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("message").equalsIgnoreCase("Exists in Cart"))
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        getWishList();
                    }
                    ((MainActivity) getActivity()).getWishList();
                    ((MainActivity) getActivity()).getCartList();
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
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("cloth_id", clothId);
                map.put("user_id", userId);
                map.put("quantity", "1");
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}
