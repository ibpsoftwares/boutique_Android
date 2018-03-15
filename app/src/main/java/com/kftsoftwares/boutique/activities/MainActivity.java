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
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.utils.Util;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.ADD_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.GET_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.REMOVE_FROM_WISHLIST;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.VIEW_CART;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout mHome, mCategory, mProfile, mSetting, mWishList, mAccount;
    public ImageView mCartView, mProfileImageHeader;
    private SharedPreferences sharedPreferences;
    public TextView mWishCountText, mCartCountText, mHeaderText;
    private Util mUtil;
    //testing
    public int mCartCount;
    private DatabaseHandler mDatabaseHandler;
    public ArrayList<String> mUserIdArrayList;


    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
            getLocalWishListData();
            getLocalCartListData();
            mProfile.setVisibility(View.GONE);
            mSetting.setVisibility(View.GONE);
            mProfileImageHeader.setVisibility(View.VISIBLE);
        } else {
            getWishList();
            getCartList();
            mProfile.setVisibility(View.VISIBLE);
            mSetting.setVisibility(View.VISIBLE);
            mProfileImageHeader.setVisibility(View.GONE);

        }

        if (!AppController.getInstance().isOnline()) {
            mUtil.checkConnection(MainActivity.this, false);
        }
    }

    public void getLocalWishListData() {

        List<CartViewModel> list = mDatabaseHandler.getAllDataOfWishlist();

        if (mUserIdArrayList != null) {
            mUserIdArrayList.clear();
        }

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                mUserIdArrayList.add(list.get(i).getClothId());
            }

            mWishCountText.setVisibility(View.VISIBLE);
            mWishCountText.setText(String.valueOf(list.size()));

        } else {
            mWishCountText.setVisibility(View.GONE);

        }

    }

    public void getLocalCartListData() {

        List<CartViewModel> list = mDatabaseHandler.getAllDataOfCart();

        if (list.size() > 0) {
            mCartCountText.setVisibility(View.VISIBLE);
            mCartCountText.setText(String.valueOf(list.size()));

        } else {
            mCartCountText.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDatabaseHandler = new DatabaseHandler(MainActivity.this);

        mUtil = new Util();

        mHome = findViewById(R.id.homeRelativeLayout);
        mCategory = findViewById(R.id.categoryRelativeLayout);
        mProfile = findViewById(R.id.profileRelativeLayout);
        mSetting = findViewById(R.id.settingRelativeLayout);
        mWishList = findViewById(R.id.wishListRelativeLayout);
        mAccount = findViewById(R.id.accountRelativeLayout);
        mCartView = findViewById(R.id.cartView);
        mWishCountText = findViewById(R.id.wishListCount);
        mHeaderText = findViewById(R.id.headerText);
        mCartCountText = findViewById(R.id.cartCount);
        mProfileImageHeader = findViewById(R.id.profileHeader);
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
        mAccount.setOnClickListener(this);
        mProfileImageHeader.setOnClickListener(this);
        mCartView.setOnClickListener(this);
        mUserIdArrayList = new ArrayList<>();
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
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        switch (v.getId()) {
            case R.id.homeRelativeLayout:

                if (f instanceof Home) {

                } else {

                    clearStack();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.
                            beginTransaction().replace(R.id.frameLayout, new Home(), "");
                    fragmentTransaction.commit();
                    changeColor(0);
                }
                break;

            case R.id.categoryRelativeLayout:
                if (f instanceof Category) {

                } else {
                    Fragment fragment = new Category();

                    changeFragment(fragment, "Category", 1);
                }
                break;

            case R.id.profileRelativeLayout:

                if (f instanceof Profile_Fragment) {

                } else {
                    Fragment profile = new Profile_Fragment();

                    changeFragment(profile, "Profile", 2);
                }
                break;

            case R.id.settingRelativeLayout:

                if (f instanceof Setting_Fragment) {

                } else {
                    changeFragment(new Setting_Fragment(), "Setting", 3);
                }
                break;

            case R.id.cartView:
                startActivity(new Intent(MainActivity.this, CartView.class));
                break;

            case R.id.accountRelativeLayout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.profileHeader:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;


            case R.id.wishListRelativeLayout:

                if (f instanceof WishList_Fragment) {

                } else {
                    changeFragment(new WishList_Fragment(), "wishlist", 4);
                }
                break;

        }
    }


    //---------------------------Change Fragment----------------------//
    public void changeFragment(Fragment fragment, String tag, int val) {

        clearStack();
        changeColor(val);
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

                    if (jsonObject.has("message") && jsonObject.getString("message") != null && jsonObject.get("message").toString().equalsIgnoreCase("Wishlist Empty")) {

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
        ;
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    //----------------GET WISHLIST DATA FROM SERVER WITHOUT LOADER-----------//
    public void getWishListWithoutLoader() {

        String tag_string_req = "string_req";


        final String userId = sharedPreferences.getString(User_ID, "");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_WISH_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message") != null && jsonObject.get("message").toString().equalsIgnoreCase("Wishlist Empty")) {

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


    //-----------------GET CART DATA----------//
    public void getCartList() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String user_id = sharedPreferences.getString(User_ID, "");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                VIEW_CART, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message") != null && jsonObject.get("message").toString().equalsIgnoreCase("Cart Empty")) {

                        mCartCountText.setVisibility(View.GONE);
                        mCartCount = 0;

                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                        // add your code here
                        if (f instanceof Setting_Fragment) {
                            mCartCountText.setVisibility(View.GONE);

                        } else if (f instanceof Profile_Fragment) {
                            mCartCountText.setVisibility(View.GONE);


                        } else {
                            mCartCountText.setVisibility(View.VISIBLE);

                        }


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
                map.put("user_id", sharedPreferences.getString(User_ID, ""));
                return map;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    //--------------DELETE DATA FROM WISH LIST--------------------------------//
    public void deleteFromWishList(final String cloth_id, final String way) {
        String tag_string_req = "string_req";

        final String userId = sharedPreferences.getString(User_ID, "");


        StringRequest strReq = new StringRequest(Request.Method.POST,
                REMOVE_FROM_WISHLIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (way.equalsIgnoreCase("home")) {
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


    //---------------------API FOR GET ADD TO WISH LIST----------------------//
    public void addToWishList(final String clothId, String way) {

        String tag_string_req = "string_req";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                ADD_WISH_LIST, new Response.Listener<String>() {

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

        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }


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
