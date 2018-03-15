package com.kftsoftwares.boutique.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.CartViewAdapter;
import com.kftsoftwares.boutique.Adapter.WishListAdapter;
import com.kftsoftwares.boutique.Fragments.WishList_Fragment;
import com.kftsoftwares.boutique.Interface.CartListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.REMOVE_FROM_CART;
import static com.kftsoftwares.boutique.utils.Constants.TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.VIEW_CART;

public class CartView extends AppCompatActivity implements CartListInterface {

    private ListView mListView;
    private SharedPreferences mSharedPreferences;
    private ArrayList<CartViewModel> mCartViewModel;
    private TextView mTotalItemCount , mAmountCount;
    private int mAmountCountValue = 0;
    private RelativeLayout no_data_image;
    private DatabaseHandler mDatabaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mDatabaseHandler = new DatabaseHandler(CartView.this);

        mListView = (ListView) findViewById(R.id.cartListView);
        ImageView backButton = findViewById(R.id.backButton);
        mTotalItemCount = findViewById(R.id.totalItemCount);
        mAmountCount = findViewById(R.id.amountCount);
        no_data_image = findViewById(R.id.no_data_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCartViewModel = new ArrayList<>();
        if (mSharedPreferences.getString(User_ID,"").equalsIgnoreCase("")) {
            getLocalCartListData();
        }
        else {
            getCartList();

        }
    }

    private void getLocalCartListData() {

        if (mCartViewModel != null) {
            mCartViewModel.clear();
        }

        mCartViewModel.addAll(mDatabaseHandler.getAllDataOfCart());

        if (mCartViewModel.size()>0)
        {
            no_data_image.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            for (int i =0 ;i<mCartViewModel.size();i++)
            {
                mAmountCountValue = mAmountCountValue + Integer.valueOf(mCartViewModel.get(i).getPrice());

                mTotalItemCount.setText("Total("+ mCartViewModel.size() +")");

                mAmountCount.setText("$" + mAmountCountValue);
            }

        }
        else {
            no_data_image.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);

        }



        CartViewAdapter cartViewAdapter = new CartViewAdapter(CartView.this, mCartViewModel, CartView.this);

        mListView.setAdapter(cartViewAdapter);
    }

    private void getCartList() {
        if (mCartViewModel != null) {
            mCartViewModel.clear();
        }
        mAmountCountValue = 0;
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(CartView.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
      final  String user_id = mSharedPreferences.getString(User_ID, "");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                VIEW_CART , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message")!=null && jsonObject.get("message").toString().equalsIgnoreCase("Cart Empty")) {

no_data_image.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    } else {

                        no_data_image.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        CartViewModel cartViewModel = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            cartViewModel = new CartViewModel();
                            cartViewModel.setDeleteStatus(jsonObject1.getString("delete_status"));
                            cartViewModel.setColour(jsonObject1.getString("colour"));
                            cartViewModel.setPrice(jsonObject1.getString("original_price"));
                            cartViewModel.setOfferPrice(jsonObject1.getString("offer_price"));
                            mAmountCountValue += Integer.valueOf(jsonObject1.getString("original_price"));
                            cartViewModel.setDescription(jsonObject1.getString("description"));
                            //cartViewModel.setProductNum(jsonObject1.getString("product_num"));
                            cartViewModel.setTitle(jsonObject1.getString("title"));
                            cartViewModel.setCategoryId(jsonObject1.getString("category_name"));
                            cartViewModel.setImage1(jsonObject1.getString("image1"));
                            cartViewModel.setClothId(jsonObject1.getString("Cloth_id"));
                            cartViewModel.setCount("1");
                            mCartViewModel.add(cartViewModel);
                        }
                        mTotalItemCount.setText("Total("+ jsonArray.length() +")");
                        mAmountCount.setText("$" + mAmountCountValue);

                        CartViewAdapter cartViewAdapter = new CartViewAdapter(CartView.this, mCartViewModel, CartView.this);

                        mListView.setAdapter(cartViewAdapter);
                        cartViewAdapter.notifyDataSetChanged();
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
                Toast.makeText(CartView.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

                Map<String, String> map = new HashMap<>();
                map.put("user_id", user_id);
                return map;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void updateCartList(String value) {
        if (mSharedPreferences.getString(User_ID,"").equalsIgnoreCase("")) {

            mDatabaseHandler.removeDataFromCart(value);
            getLocalCartListData();

        }
        else {
            removeFromCart(value);

        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updatePrice(ArrayList<CartViewModel> arrayList) {
        mAmountCountValue =0;

        for (CartViewModel cartViewModel : arrayList)
        {

            mAmountCountValue = mAmountCountValue + Integer.valueOf(cartViewModel.getPrice()) * Integer.valueOf(cartViewModel.getCount());


        }

        CartViewAdapter cartViewAdapter = new CartViewAdapter(CartView.this, arrayList, CartView.this);

        mListView.setAdapter(cartViewAdapter);
        cartViewAdapter.notifyDataSetChanged();

            mAmountCount.setText("$" + mAmountCountValue);


    }

    private void removeFromCart(final String value) {
        String tag_string_req = "string_req";
        if(mCartViewModel!=null)
        {
            mCartViewModel.clear();
        }
      final  String user_id = mSharedPreferences.getString(User_ID, "");

        final ProgressDialog pDialog = new ProgressDialog(CartView.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_CART, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    getCartList();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(CartView.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Map<String,String> map = new HashMap<>();
                map.put("cloth_id",value);
                map.put("user_id",user_id);
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
