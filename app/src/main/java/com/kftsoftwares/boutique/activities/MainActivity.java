package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Fragments.Category;
import com.kftsoftwares.boutique.Fragments.Home;
import com.kftsoftwares.boutique.Fragments.Profile_Fragment;
import com.kftsoftwares.boutique.Fragments.Setting_Fragment;
import com.kftsoftwares.boutique.Fragments.WishList_Fragment;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.utils.Util;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.ADD_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.GET_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.REMOVE_FROM_WISHLIST;
import static com.kftsoftwares.boutique.utils.Constants.TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.VIEW_CART;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout mHome, mCategory, mProfile, mSetting, mWishList;
    public ImageView mCartView;
    private SharedPreferences sharedPreferences;
    public TextView mWishCountText , mCartCountText;
    private Util mUtil;
    //testing
    public int mCartCount;

    @Override
    protected void onResume() {
        super.onResume();
        getWishList();
        getCartList();

if (AppController.getInstance().isOnline())
{
    mUtil.checkConnection(MainActivity.this,true);
}
else {
    mUtil.checkConnection(MainActivity.this,false);

}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mUtil = new Util();

        mHome = findViewById(R.id.homeRelativeLayout);
        mCategory = findViewById(R.id.categoryRelativeLayout);
        mProfile = findViewById(R.id.profileRelativeLayout);
        mSetting = findViewById(R.id.settingRelativeLayout);
        mWishList = findViewById(R.id.wishListRelativeLayout);
        mCartView = findViewById(R.id.cartView);
        mWishCountText = findViewById(R.id.wishListCount);
        mCartCountText = findViewById(R.id.cartCount);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.
                beginTransaction().add(R.id.frameLayout, new Home(), "");
        fragmentTransaction.commit();
        changeColor(0);
        mHome.setOnClickListener(this);
        mCategory.setOnClickListener(this);
        mProfile.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mWishList.setOnClickListener(this);
        mCartView.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
   /*     Card card = new Card("4242-4242-4242-4242", 12, 2019, "123");

        if (!card.validateCard()) {
            Toast.makeText(this, "Not Validate", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Validate", Toast.LENGTH_SHORT).show();
            Stripe stripe = new Stripe(MainActivity.this, "pk_test_6pRNASCoBOKtIshFeQd4XMUh");
            stripe.createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            Toast.makeText(MainActivity.this, ""+token, Toast.LENGTH_SHORT).show();
                            // Send token to your server
                        }
                        public void onError(Exception error) {
                            // Show localized error message
                            Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();


                        }
                    }
            );
        }*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeRelativeLayout:
                clearStack();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.
                        beginTransaction().replace(R.id.frameLayout, new Home(), "");
                fragmentTransaction.commit();
                changeColor(0);
                break;

            case R.id.categoryRelativeLayout:
                Fragment fragment = new Category();

                changeFragment(fragment, "Category");
                changeColor(1);
                break;

            case R.id.profileRelativeLayout:
                Fragment profile = new Profile_Fragment();

                changeFragment(profile, "Profile");
                changeColor(2);
                break;

            case R.id.settingRelativeLayout:
                changeFragment(new Setting_Fragment(), "Setting");
                changeColor(3);
                break;

            case R.id.cartView:
                startActivity(new Intent(MainActivity.this, CartView.class));

                break;

            case R.id.wishListRelativeLayout:
                changeColor(4);
                changeFragment(new WishList_Fragment(), "Setting");

                break;

        }
    }


    //---------------------------Change Fragment----------------------//
    private void changeFragment(Fragment fragment, String tag) {
        clearStack();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.
                beginTransaction().replace(R.id.frameLayout, fragment, tag);
        fragmentTransaction.commit();
    }

    //---------------------------Change Color on Custom Bar----------------------//
    private void changeColor(int val) {
        if (val == 0) {
            mHome.setBackgroundColor(ContextCompat.getColor(this, R.color.light_tabLayout));
            mCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mWishList.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
        } else if (val == 1) {
            mHome.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.light_tabLayout));
            mProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mWishList.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
        } else if (val == 2) {
            mHome.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.light_tabLayout));
            mCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mWishList.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
        } else if (val == 3) {
            mHome.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.light_tabLayout));
            mCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mWishList.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
        } else if (val == 4) {
            mHome.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mWishList.setBackgroundColor(ContextCompat.getColor(this, R.color.light_tabLayout));
            mCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
            mSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.tabLayout));
        }

    }


    //---------------------------For Clear The Stack of Fragment----------------------//
    private void clearStack() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    //----------------GET WISHLIST DATA FROM SERVER-----------//
    public void getWishList() {

        String tag_string_req = "string_req";


        final ProgressDialog pDialog = new ProgressDialog(this);
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

                    if (jsonObject.has("message") && jsonObject.getString("message")!=null && jsonObject.get("message").toString().equalsIgnoreCase("Wishlist Empty")) {

                        mWishCountText.setVisibility(View.GONE);
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("Wishlist");

                        mWishCountText.setVisibility(View.VISIBLE);
                        mWishCountText.setText(String.valueOf(jsonArray.length()));
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
            }
        }
        );
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


   //----------------GET WISHLIST DATA FROM SERVER WITHOUT LOADER-----------//
    public void getWishListWithoutLoader() {

        String tag_string_req = "string_req";




        String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.GET,
                GET_WISH_LIST + userId + "/" + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message")!=null && jsonObject.get("message").toString().equalsIgnoreCase("Wishlist Empty")) {

                        mWishCountText.setVisibility(View.GONE);
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("Wishlist");

                        mWishCountText.setVisibility(View.VISIBLE);
                        mWishCountText.setText(String.valueOf(jsonArray.length()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    //-----------------GET CART DATA----------//
    public void getCartList() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        String user_id = sharedPreferences.getString(User_ID, "");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                VIEW_CART + "/" + user_id + "/" + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message")!=null && jsonObject.get("message").toString().equalsIgnoreCase("Cart Empty")) {

                        mCartCountText.setVisibility(View.GONE);
                        mCartCount = 0;

                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        mCartCountText.setVisibility(View.VISIBLE);
                        mCartCount = jsonArray.length();

                        mCartCountText.setText(String.valueOf(jsonArray.length()));
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
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }

        );

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    //--------------DELETE DATA FROM WISH LIST--------------------------------//
    public void deleteFromWishList(final String cloth_id ,final String way) {
        String tag_string_req = "string_req";

        final String userId = sharedPreferences.getString(User_ID, "");



        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_WISHLIST + "/" + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (way.equalsIgnoreCase("home"))
                    {
                    //    Home fragment = (Home) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                   //     fragment.getAllProducts();

                    }

                    getWishListWithoutLoader();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    //---------------------API FOR GET ADD TO WISH LIST----------------------//
    public void addToWishList(final  String clothId,String way) {

        String tag_string_req = "string_req";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                ADD_WISH_LIST + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // new com.kftsoftwares.boutique.Utils.Util().showSingleOkAlert(Productdetails.this,jsonObject.getString("message"),"Success Add to WishList");

                    getWishListWithoutLoader();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sharedPreferences.getString(User_ID, ""));
                params.put("cloth_id", clothId);
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
