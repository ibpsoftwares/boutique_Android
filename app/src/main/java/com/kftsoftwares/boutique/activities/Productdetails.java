package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.boutique.Adapter.SingleProductRecyclerView;
import com.kftsoftwares.boutique.Adapter.ViewPagerAdapterForSingleProduct;
import com.kftsoftwares.boutique.Interface.RelativeProductClick;
import com.kftsoftwares.boutique.Models.Banner;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.Models.Image;
import com.kftsoftwares.boutique.Models.SingleProduct;
import com.kftsoftwares.boutique.Models.Size;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.ADD_TO_CART;
import static com.kftsoftwares.boutique.utils.Constants.ADD_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.GET_SINGLE_PRODUCT;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class Productdetails extends AppCompatActivity implements View.OnClickListener, RelativeProductClick {

    private String mId, mCat_ID, mClotheId;
    private ArrayList<Image> mSingleProductImage;
    private ArrayList<Size> mSingleProductSize;
    private ArrayList<String> mImageString;
    private ArrayList<SingleProduct> mSingleModel;
    private ViewPager mViewPager;
    private TextView mDescreption, mPrice, mName, mOldPrice;
    private SharedPreferences mSharedPreferences;

    private LinearLayout mDots;
    private LinearLayout mSizeLinearLayout;
    private TextView mBrandName;
    private ScrollView mParentScrollView;
    private NestedScrollView mChildScrollView;
    private String mSizeDetail = "";
    private String mSingleStockSize = "";
    public ArrayList<String> mUserIdArrayList;
    private DatabaseHandler mDatabaseHandler;
    private String mSizeID;
    private ImageView[] ivArrayDotsPager;
    private RecyclerView mRecyclerView;
    private ArrayList<Banner> mBannerList;
    private SingleProductRecyclerView mHomeSubRecyclerView;
    private String mCartCount ="0";
    private ImageView mOutOfStock , mImageShade;
    private TextView mCartCountTextView;
    private LinearLayout mOutOfStockLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        mDescreption = findViewById(R.id.description);
        mOutOfStock = findViewById(R.id.outOfStock);
        mOutOfStockLinearLayout = findViewById(R.id.outOfStockLinerLayout);
        mImageShade = findViewById(R.id.imageShade);


        mDatabaseHandler = new DatabaseHandler(Productdetails.this);
        mUserIdArrayList = new ArrayList<>();
        mSingleModel = new ArrayList<>();
        mBannerList = new ArrayList<>();

        getLocalWishListData();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getString("id") != null) {
                mId = b.getString("id");
            }
            if (b.getString("cat_id") != null) {
                mCat_ID = b.getString("cat_id");
            }
            if (b.getString("cart_size") != null) {
          //      mCartCount = b.getString("cart_size");
            }

        }

        mCartCount = ((AppController) this.getApplication()).getSomeVariable();


        mViewPager = findViewById(R.id.viewPager);
        mPrice = findViewById(R.id.newPrice);
        mOldPrice = findViewById(R.id.oldPrice);
        mRecyclerView = findViewById(R.id.recycler_View);
        mName = findViewById(R.id.name);
        mDots = findViewById(R.id.pager_dots);
        mSizeLinearLayout = findViewById(R.id.linearLayoutForSizes);
        mCartCountTextView = findViewById(R.id.cartCount);
        mBrandName = findViewById(R.id.brandName);
        mSingleProductImage = new ArrayList<>();
        mSingleProductSize = new ArrayList<>();
        mImageString = new ArrayList<>();
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        ImageView backButton = findViewById(R.id.backButton);
        ImageView cart = findViewById(R.id.cartView);



        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Productdetails.this, CartView.class));
                finish();
            }
        });
        RelativeLayout backLayout = findViewById(R.id.relativeLayoutBack);
        mParentScrollView = findViewById(R.id.parentScrollView);
        mChildScrollView = findViewById(R.id.childScrollView);
        mOldPrice.setPaintFlags(mOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addToWishList = findViewById(R.id.addToWishList);
        Button addToCart = findViewById(R.id.addToCart);
        addToWishList.setOnClickListener(this);
        addToCart.setOnClickListener(this);

        getSingleProduct(mId, mCat_ID);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setBackground(ContextCompat.getDrawable(Productdetails.this, R.drawable.un_select));
                }
                ivArrayDotsPager[position].setBackground(ContextCompat.getDrawable(Productdetails.this, R.drawable.selected_dots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mParentScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                mChildScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                //  We will have to follow above for all scrollable contents
                return false;
            }
        });

        mChildScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                // this will disallow the touch request for parent scroll on touch of child view
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });



        updateTextOnCartCount();

    }

    @Override
    protected void onResume() {
        super.onResume();

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

        } else {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addToWishList:
                if (mSharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
                    if (mDatabaseHandler.CheckIsDataAlreadyInWishList(mId)) {
                        Toast.makeText(this, "Already in wishList", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mSizeDetail.equalsIgnoreCase("")) {
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.bounce);
                            mSizeLinearLayout.startAnimation(animation);
                            Toast.makeText(this, "Please Select the size first", Toast.LENGTH_SHORT).show();
                        } else {

                            CartViewModel cartViewModel = new CartViewModel();
                            cartViewModel.setClothId(mSingleModel.get(0).getId());
                            cartViewModel.setTitle(mSingleModel.get(0).getTitle());
                            cartViewModel.setImage1(mSingleProductImage.get(0).getImage());
                            cartViewModel.setPrice(mSingleModel.get(0).getPrice());
                            cartViewModel.setCategoryId(mSingleModel.get(0).getCategoryId());
                            cartViewModel.setStock_size(mSingleModel.get(0).getStockSize());
                            cartViewModel.setProduct_id(mSingleModel.get(0).getId());

                            cartViewModel.setOnlyStockSizeForlocal("0");
                            cartViewModel.setSize("noData");
                            cartViewModel.setSize_id("");
                            cartViewModel.setCat("wishList");
                            cartViewModel.setCount("1");
                            mDatabaseHandler.add(cartViewModel);
                            Toast.makeText(this, "Added in wishList", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {

                    if (mSizeDetail.equalsIgnoreCase("")) {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.bounce);
                        mSizeLinearLayout.startAnimation(animation);
                        Toast.makeText(this, "Please Select the size first", Toast.LENGTH_SHORT).show();
                    } else {
                        addToWishList();
                    }
                }
                break;

            case R.id.addToCart:
                if (mSharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {

                    if (mSizeDetail.equalsIgnoreCase("")) {
                        Toast.makeText(this, "Please select the size first", Toast.LENGTH_SHORT).show();
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.bounce);
                        mSizeLinearLayout.startAnimation(animation);
                    } else if (mDatabaseHandler.CheckIsDataAlreadyInDBorNotWithSize(mId, mSizeDetail)) {
                        Toast.makeText(this, "Already in cart", Toast.LENGTH_SHORT).show();

                    } else {
                        CartViewModel cartViewModel = new CartViewModel();
                        cartViewModel.setClothId(mSingleModel.get(0).getId());
                        cartViewModel.setTitle(mSingleModel.get(0).getTitle());
                        cartViewModel.setImage1(mSingleProductImage.get(0).getImage());
                        cartViewModel.setPrice(mSingleModel.get(0).getPrice());
                        cartViewModel.setCategoryId(mSingleModel.get(0).getCategoryId());
                        cartViewModel.setProduct_id(mSingleModel.get(0).getId());
                        cartViewModel.setStock_size(mSingleStockSize);
                        cartViewModel.setOnlyStockSizeForlocal("0");
                        cartViewModel.setSize(mSizeDetail);
                        cartViewModel.setSize_id(mSizeID);
                        cartViewModel.setCat("cart");
                        cartViewModel.setCount("1");
                        mDatabaseHandler.add(cartViewModel);
                        Toast.makeText(this, "Added in cart", Toast.LENGTH_SHORT).show();

                        mCartCount = String.valueOf(Integer.valueOf(mCartCount)+1);

                        updateTextOnCartCount();
                    }
                } else {
                    if (mSizeDetail.equalsIgnoreCase("")) {
                        Toast.makeText(this, "Please Select the size first", Toast.LENGTH_SHORT).show();
                    } else {
                        addToCart();
                    }
                }
                break;
        }
    }

    private void updateTextOnCartCount() {

        if (Integer.valueOf(mCartCount)>0)
        {
            mCartCountTextView.setVisibility(View.VISIBLE);
            mCartCountTextView.setText(mCartCount);
        }
        else {
            mCartCountTextView.setVisibility(View.GONE);

        }

    }


    //---------------------API FOR GET SINGLE PRODUCT DETAIL----------------------//

    private void getSingleProduct(final String id, final String category_id) {
        String tag_string_req = "string_req";
        if (mBannerList != null) {
            mBannerList.clear();
        }
        if (mSingleProductSize != null) {
            mSingleProductSize.clear();
        }
        if (mSingleProductImage != null) {
            mSingleProductImage.clear();
        }
        if (mImageString != null) {
            mImageString.clear();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHomeSubRecyclerView = new SingleProductRecyclerView(Productdetails.this, new ArrayList<Banner>(), Productdetails.this);
        mRecyclerView.setAdapter(mHomeSubRecyclerView);



        final ProgressDialog pDialog = new ProgressDialog(Productdetails.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_SINGLE_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                 pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("cloth");

                    if (jsonObject.has("relatedProducts")) {

                        JSONArray relatedProducts = jsonObject.getJSONArray("relatedProducts");

                        Banner banner = null;
                        for (int i = 0; i < relatedProducts.length(); i++) {
                            banner = new Banner();
                            JSONObject jsonObject1 = relatedProducts.getJSONObject(i);
                            banner.setCategory_id(jsonObject1.getString("category_id"));
                            banner.setImage(jsonObject1.getString("image"));
                            banner.setName(jsonObject1.getString("title"));
                            banner.setId(jsonObject1.getString("id"));
                            banner.setStock_size(jsonObject1.getString("total_stock"));

                            mBannerList.add(banner);
                        }
                    }

                    SingleProduct singleProduct = null;
                    Image imageSingle = null;
                    Size size = null;

                    JSONObject object = jsonArray.getJSONObject(0);
                    singleProduct = new SingleProduct();
                    singleProduct.setDescription(object.getString("description"));
                    singleProduct.setTitle(object.getString("title"));
                    singleProduct.setPrice(object.getString("original_price"));
                    JSONArray jsonArray1 = object.getJSONArray("images");
                    JSONArray jsonArray2 = object.getJSONArray("size");

                    if (mSingleProductImage != null) {
                        mSingleProductImage.clear();
                    }
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject object1 = jsonArray1.getJSONObject(j);

                        imageSingle = new Image();
                        imageSingle.setImage(object1.getString("image"));
                        mImageString.add(object1.getString("image"));
                        mSingleProductImage.add(imageSingle);

                    }

                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject object1 = jsonArray2.getJSONObject(j);
                        size = new Size();
                        size.setSize(object1.getString("size"));
                        size.setSize_id(object1.getString("id"));
                        size.setStock(object1.getString("stock"));
                        mSingleProductSize.add(size);

                    }
                    mDescreption.setText(object.getString("description"));


                    if (object.getString("offer_price") != null &&

                            !object.getString("offer_price").equalsIgnoreCase("null")) {

                        mOldPrice.setText(Html.fromHtml(mSharedPreferences.getString(Symbol, "")) + " " + object.getString("offer_price"));

                        mOldPrice.setVisibility(View.VISIBLE);
                        mPrice.setText(Html.fromHtml(mSharedPreferences.getString(Symbol, "")) + " " + object.getString("original_price"));
                        singleProduct.setPrice(object.getString("offer_price"));

                    } else {
                        mOldPrice.setVisibility(View.GONE);
                        mPrice.setText(Html.fromHtml(mSharedPreferences.getString(Symbol, "")) + " " + object.getString("original_price"));
                        singleProduct.setPrice(object.getString("original_price"));

                    }


                    // mPrice.s
                    //
                    //
                    // etText(object.getString("offer_price"));
                    mName.setText(object.getString("brand"));

                    if(object.getString("total_stock").equalsIgnoreCase("0"))
                    {
                      mOutOfStock.setVisibility(View.VISIBLE);
                        mImageShade.setVisibility(View.VISIBLE);
                        mOutOfStockLinearLayout.setVisibility(View.VISIBLE);
                    }
                    else{

                        mOutOfStock.setVisibility(View.GONE);
                        mImageShade.setVisibility(View.GONE);
                        mOutOfStockLinearLayout.setVisibility(View.GONE);

                    }


                    mBrandName.setText(object.getString("title"));
                    mClotheId = object.getString("id");
                    singleProduct.setId(object.getString("id"));
                    singleProduct.setTitle(object.getString("title"));
                    singleProduct.setCategoryId(object.getString("category_id"));
                    singleProduct.setStockSize(object.getString("total_stock"));
                    mSingleModel.add(singleProduct);



                    System.out.print(mBannerList);
                    mHomeSubRecyclerView.update(mBannerList);
              /*      mRecyclerView.invalidate();
                    mHomeSubRecyclerView.notifyDataSetChanged();
              */

              ViewPagerAdapterForSingleProduct viewPagerAdapter = new ViewPagerAdapterForSingleProduct(Productdetails.this, mSingleProductImage, mImageString);

                    mViewPager.setAdapter(viewPagerAdapter);
                    setupPagerIndidcatorDots();
                    mSizeDetail="";
                    setUpSizes();

                    ivArrayDotsPager[0].setBackground(ContextCompat.getDrawable(Productdetails.this, R.drawable.selected_dots));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(Productdetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cloth_id", id);
                params.put("category_id", category_id);
                return params;
            }

        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    //----------------ADD SIZES BUTTON-------------------//

    private void setUpSizes() {
        mSizeLinearLayout.removeAllViews();
        // create two layouts to hold buttons
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        params.gravity = Gravity.CENTER;
        // create buttons in a loop
        for (int i = 0; i < mSingleProductSize.size(); i++) {
            final TextView button = new TextView(this);
            button.setText(String.valueOf(mSingleProductSize.get(i).getSize()));
            // R.id won't be generated for us, so we need to create one
            button.setId(Integer.valueOf(mSingleProductSize.get(i).getSize_id()));
            button.setLayoutParams(params);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(ContextCompat.getColor(this, R.color.black));
            button.setTextSize(16);
            if (mSingleProductSize.get(i).getStock().equalsIgnoreCase("0")) {
                button.setBackground(ContextCompat.getDrawable(Productdetails.this, R.drawable.round_button_disable));
                button.setClickable(false);
                button.setFocusable(false);

            } else
            {
                if (String.valueOf(mSingleProductSize.get(i).getSize()).equalsIgnoreCase(mSizeDetail)) {
                button.setBackground(ContextCompat.getDrawable(Productdetails.this, R.drawable.round_button_colored));
                button.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.round_button));
                button.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
            // add our event handler (less memory than an anonymous inner class)
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSingleProductSize.get(finalI).getStock().equalsIgnoreCase("0")) {


                    }
                    else {
                        mSizeDetail = button.getText().toString();
                        mSizeID = String.valueOf(button.getId());
                        mSingleStockSize = mSingleProductSize.get(finalI).getStock() ;
                        setUpSizes();
                    }
                }
            });
            // add generated button to view
            mSizeLinearLayout.addView(button);
        }
    }

    //---------------------API FOR GET ADD TO WISH LIST----------------------//
    private void addToWishList() {

        String tag_string_req = "string_req";


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ADD_WISH_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(Productdetails.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    // new com.kftsoftwares.boutique.Utils.Util().showSingleOkAlert(Productdetails.this,jsonObject.getString("message"),"Success Add to WishList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(Productdetails.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", mSharedPreferences.getString(User_ID, ""));
                params.put("cloth_id", mClotheId);
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //----------------ADD TO CART API-------------------//
    public void addToCart() {
        String tag_string_req = "string_req";


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("0"))
                    {
                        Log.e("AddToCart","Product is Already in cart");
                    }
                    else {
                        mCartCount = String.valueOf(Integer.valueOf(mCartCount)+1);
                        updateTextOnCartCount();
                    }
                    Toast.makeText(Productdetails.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(Productdetails.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", mSharedPreferences.getString(User_ID, ""));
                params.put("cloth_id", mClotheId);
                params.put("quantity", "1");
                params.put("size_id", mSizeID);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //----------------ADD IMAGES ON VIEWPAGER-------------------//

    private void setupPagerIndidcatorDots() {
        mDots.removeAllViews();
        ivArrayDotsPager = new ImageView[mSingleProductImage.size()];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(Productdetails.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 200);
            params.setMargins(10, 10, 10, 10);
            ivArrayDotsPager[i].setLayoutParams(params);
            //   ivArrayDotsPager[i].setBackground(ContextCompat.getDrawable(this,R.drawable.un_select));
            //ivArrayDotsPager[i].setAlpha(0.4f);
            ivArrayDotsPager[i].setBackground(ContextCompat.getDrawable(Productdetails.this, R.drawable.un_select));
            ivArrayDotsPager[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivArrayDotsPager[i].setClickable(true);
            ivArrayDotsPager[i].setFocusable(true);
            ivArrayDotsPager[i].setDuplicateParentStateEnabled(true);
            final int val = i;
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(val);
                    view.setAlpha(1);
                }
            });
            Glide.with(this).load(mSingleProductImage.get(i).getImage())
                    .crossFade()
                    .fitCenter()
                    .dontTransform()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivArrayDotsPager[i]);

            mDots.addView(ivArrayDotsPager[i]);
            mDots.bringToFront();
        }
    }

    @Override
    public void productData(String id, String category_id) {
        getSingleProduct(id, category_id);


        mParentScrollView.post(new Runnable() {
            public void run() {
                mParentScrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }
}
