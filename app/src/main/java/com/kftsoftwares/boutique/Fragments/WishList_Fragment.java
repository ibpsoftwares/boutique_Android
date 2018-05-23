package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.WishListAdapter;
import com.kftsoftwares.boutique.Interface.WishListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.Models.Size;
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

import static com.kftsoftwares.boutique.utils.Constants.GET_SIZES;
import static com.kftsoftwares.boutique.utils.Constants.GET_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.MOVETOCART;
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
    private WishListAdapter mWishListAdapter;
    private SharedPreferences sharedPreferences;
    private MainActivity mContext;
    private String mCheckSize="";
    private String mSizeId="";
    private ArrayList<Size> mSizes;
    private int mPositionForMove;
    private String mClothIdForMove;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity) context);
    }


    @Override
    public void onResume() {
        super.onResume();
        mContext.mCartView.setVisibility(View.VISIBLE);
        mContext.mHeaderText.setText("Wishlist");

        if (mContext.mCartCount != 0) {
            mContext.mCartCountText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list_, container, false);
        mWishListAdapter = new WishListAdapter(mContext, new ArrayList<CartViewModel>(), WishList_Fragment.this);
        mListview = view.findViewById(R.id.listView);
        mWishListRelativeLayout = view.findViewById(R.id.no_data_image);
        mDatabaseHandler = new DatabaseHandler(mContext);
        sharedPreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mListview.setAdapter(mWishListAdapter);
        mCartViewModels = new ArrayList<>();
        mSizes = new ArrayList<>();
        mClothIds = new ArrayList<>();
        if (mClothIds != null) {
            mClothIds.clear();
        }

        if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
            getLocalWishListData();
        } else {
            getWishList();

        }
        mContext.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSizeId.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please select size", Toast.LENGTH_SHORT).show();
                }
                else {
                    moveToCart(mClothIdForMove, mPositionForMove, mCheckSize, mCartViewModels, mSizeId);
                    mContext.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        return view;
    }


    private void getLocalWishListData() {

        if (mCartViewModels != null) {
            mCartViewModels.clear();
        }

        mCartViewModels.addAll(mDatabaseHandler.getAllDataOfWishlist());

        if (mCartViewModels.size() > 0) {
            mWishListRelativeLayout.setVisibility(View.GONE);
            mListview.setVisibility(View.VISIBLE);
            for (int i = 0; i < mCartViewModels.size(); i++) {
                mClothIds.add(mCartViewModels.get(i).getClothId());
            }
            mWishListAdapter.UpdateData(mCartViewModels);
        } else {
            mWishListRelativeLayout.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.GONE);
        }

    }


    //----------------GET WISHLIST DATA FROM SERVER-----------//
    private void getWishList() {
        String tag_string_req = "string_req";
        if (mCartViewModels != null) {
            mCartViewModels.clear();
        }

        mContext.showProgressBar(true);

        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_WISH_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

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

                        mWishListAdapter.UpdateData(mCartViewModels);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public void updateWishList(String value, int position, ArrayList<CartViewModel> cartViewModels) {
        if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
            mDatabaseHandler.removeDataFromWishList(value);
            getLocalWishListData();
            mContext.getLocalWishListData();
        } else {
            deleteFromWishList(value, cartViewModels, position);
        }
    }


    private void setDataWhileLocalRemoveArry(ArrayList<CartViewModel> cartViewModels, int position) {

        mCartViewModels.remove(position);
        if (mCartViewModels.size() > 0) {
            mWishListRelativeLayout.setVisibility(View.GONE);
            mListview.setVisibility(View.VISIBLE);

            mWishListAdapter.UpdateData(mCartViewModels);
        } else {
            mWishListRelativeLayout.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.GONE);

        }

    }


    @Override
    public void moveToWishList(String clothId, int positon, ArrayList<CartViewModel> cartViewModels) {

        mCheckSize="";
        mSizeId="";
        getSizesFromServer(clothId);
        mClothIdForMove = clothId;
        mPositionForMove = positon;
        if (mCartViewModels != null) {
            mCartViewModels.clear();
        }
        mCartViewModels.addAll(cartViewModels);

    }

    private void moveToCart(String cloth_id, int position, String size, ArrayList<CartViewModel> cartViewModels , String mSizeId) {
        if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {

            if (mDatabaseHandler.CheckIsDataAlreadyInDBorNotWithSize(cloth_id, size)) {
                Toast.makeText(mContext, "Already Exists In Cart", Toast.LENGTH_SHORT).show();
            } else {
                mDatabaseHandler.updateCategoryWithSize("cart", cloth_id, size, mSizeId);
                getLocalWishListData();
                mContext.getLocalCartListData();
                mContext.getLocalWishListData();
            }
        } else {
            moveToWishListMehtod(cloth_id, cartViewModels, position);

        }
    }


    //--------------DELETE DATA FROM WISH LIST--------------------------------//
    private void deleteFromWishList(final String cloth_id, final ArrayList<CartViewModel> cartViewModels, final int positon) {
        String tag_string_req = "string_req";

        final String userId = sharedPreferences.getString(User_ID, "");

        mContext.showProgressBar(true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_WISHLIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    setDataWhileLocalRemoveArry(cartViewModels, positon);

                    mContext.getWishListWithoutLoader();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
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
    private void moveToWishListMehtod(final String clothId, final ArrayList<CartViewModel> cartViewModels, final int position) {
        String tag_string_req = "string_req";

        mContext.showProgressBar(true);

        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                MOVETOCART, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("message").equalsIgnoreCase("Exists in Cart")) {
                        Toast.makeText(mContext, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        setDataWhileLocalRemoveArry(cartViewModels, position);


                    }
                    mContext.getCartList();
                    mContext.getWishListWithoutLoader();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
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

                Map<String, String> map = new HashMap<>();
                map.put("cloth_id", clothId);
                map.put("user_id", userId);
                map.put("quantity", "1");
                map.put("size_id", mSizeId);
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void setUpSizes(final ArrayList<Size> size) {

        mContext.mBottomContainer.removeAllViews();
        // create two layouts to hold buttons
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        params.gravity = Gravity.CENTER;
        // create buttons in a loop
        for (int i = 0; i < size.size(); i++) {
            final TextView button = new TextView(mContext);
            button.setText(String.valueOf(size.get(i).getSize()));
            // R.id won't be generated for us, so we need to create one
            button.setId(Integer.valueOf(size.get(i).getSize_id()));
            button.setLayoutParams(params);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            button.setTextSize(10);
            if (mCheckSize.equalsIgnoreCase(String.valueOf(size.get(i).getSize()))) {
                button.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_button_colored));
                button.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                button.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_button));
                button.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }
            // add our event handler (less memory than an anonymous inner class)
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckSize = button.getText().toString();
                    mSizeId = String.valueOf(button.getId());
                    button.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_button_colored));
                    button.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    setUpSizes(size);
                }
            });
            // add generated button to view
            mContext.mBottomContainer.addView(button);
        }
    }


    private void getSizesFromServer(final String cloth_id) {

        if (mSizes != null) {
            mSizes.clear();
        }
        final String userId = sharedPreferences.getString(User_ID, "");
        String tag_string_req = "string_req";

        mContext.showProgressBar(true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_SIZES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    Size size = null;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("sizes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("Size");
                        size = new Size();
                        size.setSize(jsonObject2.getString("size"));
                        size.setSize_id(jsonObject2.getString("id"));

                        mSizes.add(size);

                    }

                    setUpSizes(mSizes);
                    mContext.showBottomSheet();

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
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

                Map<String, String> map = new HashMap<>();
                map.put("cloth_id", cloth_id);
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
