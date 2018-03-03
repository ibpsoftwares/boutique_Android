package com.kftsoftwares.boutique.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.kftsoftwares.boutique.Models.WishListModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
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
import static com.kftsoftwares.boutique.utils.Constants.TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class WishList_Fragment extends Fragment implements WishListInterface {

    private ListView mListview;
    private ArrayList<WishListModel> mWishListModels;
    private RelativeLayout mWishListRelativeLayout;

    private SharedPreferences sharedPreferences;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).mCartView.setVisibility(View.VISIBLE);
        if (((MainActivity)getActivity()).mCartCount!=0)
        {
            ((MainActivity)getActivity()).mCartCountText.setVisibility(View.VISIBLE);

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
        mWishListModels = new ArrayList<>();
        getWishList();
        return view;
    }

    //----------------GET WISHLIST DATA FROM SERVER-----------//
    private void getWishList() {

        String tag_string_req = "string_req";
        if (mWishListModels != null) {
            mWishListModels.clear();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.GET,
                GET_WISH_LIST + userId + "/" + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("message") && jsonObject.getString("message")!=null && jsonObject.getString("message").equalsIgnoreCase("Wishlist Empty")) {

                        mWishListRelativeLayout.setVisibility(View.VISIBLE);
                        mListview.setVisibility(View.GONE);
                    } else {
                        mWishListRelativeLayout.setVisibility(View.GONE);
                        mListview.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("Wishlist");

                        WishListModel wishListModel = null;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            wishListModel = new WishListModel();
                            wishListModel.setCatrgoryId(object.getString("category_id"));
                            wishListModel.setClothId(object.getString("Cloth_id"));
                            wishListModel.setColour(object.getString("colour"));
                            wishListModel.setImage1(object.getString("image1"));
                            wishListModel.setDescreption(object.getString("description"));
                            wishListModel.setTitle(object.getString("title"));
                            wishListModel.setPrice(object.getString("price"));
                            mWishListModels.add(wishListModel);
                        }

                        WishListAdapter wishListAdapter = new WishListAdapter(getActivity(), mWishListModels, WishList_Fragment.this);

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
        );
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public void updateWishList(String value) {
        deleteFromWishList(value);
    }

    @Override
    public void  moveToWishList(String wishid, String clothId) {
        moveToWishListMehtod(wishid,clothId);
    }

    //--------------DELETE DATA FROM WISH LIST--------------------------------//
    private void deleteFromWishList(final String cloth_id) {
        String tag_string_req = "string_req";
        if (mWishListModels != null) {
            mWishListModels.clear();
        }
       final String userId = sharedPreferences.getString(User_ID, "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_WISHLIST + "/" + TOKEN, new Response.Listener<String>() {

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

    //--------------Move To  WISH LIST--------------------------------//
    private void moveToWishListMehtod(final String WishID , final String clothId) {
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                MOVETOWISHLIST + "/" + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    getWishList();
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
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("cloth_id", clothId);
                map.put("user_id", userId);
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
