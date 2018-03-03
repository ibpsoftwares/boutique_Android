package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
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
import com.kftsoftwares.boutique.Interface.CartListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
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
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.VIEW_CART;

public class CartView extends AppCompatActivity implements CartListInterface {

    private ListView mListView;
    private SharedPreferences mSharedPreferences;
    private ArrayList<CartViewModel> mCartViewModel;
    private TextView mTotalItemCount , mAmountCount;
    private int mAmountCountValue;
    private RelativeLayout no_data_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

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
        getCartList();
    }

    private void getCartList() {
        if (mCartViewModel != null) {
            mCartViewModel.clear();
        }
        mAmountCountValue = 0;
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(CartView.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        String user_id = mSharedPreferences.getString(User_ID, "");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                VIEW_CART + "/" + user_id + "/" + TOKEN, new Response.Listener<String>() {

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

                            mAmountCountValue += Integer.valueOf(jsonObject1.getString("original_price"));
                            cartViewModel.setDescription(jsonObject1.getString("description"));
                            //cartViewModel.setProductNum(jsonObject1.getString("product_num"));
                            cartViewModel.setTitle(jsonObject1.getString("title"));
                            cartViewModel.setCategoryId(jsonObject1.getString("category_name"));
                            cartViewModel.setImage1(jsonObject1.getString("image1"));
                            cartViewModel.setClothId(jsonObject1.getString("Cloth_id"));
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

        );

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void updateCartList(String value) {

        removeFromCart(value);

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
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_CART +"/"+TOKEN , new Response.Listener<String>() {

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
