package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.FilterListViewAdapter;
import com.kftsoftwares.boutique.Adapter.GridViewAdapter;
import com.kftsoftwares.boutique.Adapter.SortListViewAdapter;
import com.kftsoftwares.boutique.Interface.FilterListView;
import com.kftsoftwares.boutique.Interface.SortListview;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.Models.FilterDataModel;
import com.kftsoftwares.boutique.Models.GetAllProductModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.utils.Util;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.ADD_WISH_LIST;
import static com.kftsoftwares.boutique.utils.Constants.GET_ALL_CATEGORIES_BY_ID;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.PRICE_RANGE;
import static com.kftsoftwares.boutique.utils.Constants.REMOVE_FROM_WISHLIST;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class ProductList extends AppCompatActivity implements View.OnClickListener, FilterListView, SortListview {
    private GridView mGridView;
    private ArrayList<GetAllProductModel> mGetAllProductModels;
    private ArrayList<GetAllProductModel> mSortedGetAllProductModels;
    private ArrayList<GetAllProductModel> mGetSortedList;
    private String mId ="" , mOfferID="";
    private RelativeLayout mlinearLayout;
    private ArrayList<String> mPriceArrayList;
    private LinearLayout mFilter;
    private ArrayList<FilterDataModel> mFilterDataModel;
    private ListView mFilterListView;
    private int mVal = -1;
    private FilterListViewAdapter mFilterListViewAdapter;
    private String mSelectedId = "";
    private PopupWindow popupWindow;
    private GridViewAdapter mGridViewAdapter;
    private SharedPreferences sharedPreferences;
    public AlertDialog mAlert;
    public ArrayList<String> mUserIdArrayList;
    private DatabaseHandler mDatabaseHandler;
    public String mCartCount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_new);
        mDatabaseHandler = new DatabaseHandler(ProductList.this);
        mUserIdArrayList = new ArrayList<>();
        getLocalWishListData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("id") != null) {
                mId = bundle.getString("id");
            }
            if (bundle.getString("offer_id") != null) {
                mOfferID = bundle.getString("offer_id");
            }
            if (bundle.getString("cart_id") != null) {
                mCartCount = bundle.getString("cart_id");
            }
        }
        mGetAllProductModels = new ArrayList<>();
        mSortedGetAllProductModels = new ArrayList<>();
        mGetSortedList = new ArrayList<>();
        mGridView = findViewById(R.id.gridView);
        ImageView mBackButton = findViewById(R.id.backButton);
        mlinearLayout = findViewById(R.id.no_data_image);
        mFilter = findViewById(R.id.filter);
        LinearLayout sort = findViewById(R.id.sort);
        mFilter.setOnClickListener(this);
        sort.setOnClickListener(this);
        final EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                mGridViewAdapter.filter(text);
            }
        });
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        getProductList();

        getPriceList();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFilterDataModel = new ArrayList<>();
        mFilterListViewAdapter = new FilterListViewAdapter(ProductList.this, new ArrayList<FilterDataModel>(), mVal, ProductList.this);
        mGridViewAdapter = new GridViewAdapter(ProductList.this, new ArrayList<GetAllProductModel>());
        mGridView.setAdapter(mGridViewAdapter);
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
    //---------------------GET PRODUCT LIST BY CATEGORY -----------------//

    private void getProductList() {
        if (mGetAllProductModels != null) {
            mGetAllProductModels.clear();
        }

        String tag_string_req = "string_req";
        final String userId = sharedPreferences.getString(User_ID, "");

        final ProgressDialog pDialog = new ProgressDialog(ProductList.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_ALL_CATEGORIES_BY_ID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message") && jsonObject.getString("message") != null
                            && jsonObject.getString("message").equalsIgnoreCase("no match found")) {
                        //Toast.makeText(ProductList.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        mGridView.setVisibility(View.GONE);
                        mlinearLayout.setVisibility(View.VISIBLE);

                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("cloths");
                        GetAllProductModel getAllProductModel = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            getAllProductModel = new GetAllProductModel();
                            getAllProductModel.setId(jsonObject1.getString("id"));
                            getAllProductModel.setCategoryId(jsonObject1.getString("category_id"));
                            getAllProductModel.setTitle(jsonObject1.getString("title"));
                            getAllProductModel.setPrice(jsonObject1.getString("original_price"));
                            getAllProductModel.setOfferPrice(jsonObject1.getString("offer_price"));
                            getAllProductModel.setBrandName(jsonObject1.getString("brand"));
                            getAllProductModel.setCategoryName(jsonObject1.getString("category_name"));
                            getAllProductModel.setStock_size(jsonObject1.getString("total_stock"));
                            getAllProductModel.setImage1(jsonObject1.getString("image"));
                            getAllProductModel.setDescription("description");
                            getAllProductModel.setColour("colour");

                            if (jsonObject1.has("wishlist")) {

                                if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("") && mUserIdArrayList.contains(jsonObject1.getString("id"))) {
                                    getAllProductModel.setWish_list("1");

                                } else {
                                    getAllProductModel.setWish_list(jsonObject1.getString("wishlist"));

                                }

                            } else {
                                getAllProductModel.setWish_list("0");
                            }

                            mGetAllProductModels.add(getAllProductModel);
                        }
                        mlinearLayout.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                        mGridViewAdapter.update(mGetAllProductModels);
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
                Toast.makeText(ProductList.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                if (!mId.equalsIgnoreCase("")) {
                    map.put("category_id", mId);
                    map.put("user_id", userId);
                } else {
                    if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase("")) {
                        map.put("offer_id", mOfferID);
                    } else {
                        map.put("user_id", userId);
                        map.put("offer_id", mOfferID);
                    }
                }
                return map;
            }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter:
                showDialog();
                break;
            case R.id.sort:
//                PopupWindow popUp = popupWindowsort();
//              popUp.showAsDropDown(v, -1, -1); // show popup like dropdown list
                showSortDialog();
                break;
        }
    }

    //-----------------------Show Filter Dialog---------------------------//
    private void showDialog() {
        LayoutInflater lf = LayoutInflater.from(this);
        // This adds XML elements as a custom view (optional):
        final View customElementsView = lf.inflate(R.layout.dialog_filter, null);

        ListView filterListView = customElementsView.findViewById(R.id.listView);
        filterListView.setAdapter(mFilterListViewAdapter);


        Button button = customElementsView.findViewById(R.id.ok_Button);
        Button cancel = customElementsView.findViewById(R.id.cancel);

        final AlertDialog alert = new AlertDialog.Builder(this)
                // This adds the custom view to the Dialog (optional):
                .setView(customElementsView)
                .setTitle(null)
                .setMessage(null)
                .create();

        mFilterListViewAdapter.updateValue(mVal, mFilterDataModel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getProductList("", mSelectedId);

                if (mSelectedId.equalsIgnoreCase("")) {


                    if (mGetAllProductModels != null && mGetAllProductModels.size() > 0) {
                        mlinearLayout.setVisibility(View.GONE);
                        GridViewAdapter gridViewAdapter = new GridViewAdapter(ProductList.this, mGetAllProductModels);
                        mGridView.setAdapter(gridViewAdapter);
                        gridViewAdapter.notifyDataSetChanged();
                    } else {
                        mlinearLayout.setVisibility(View.VISIBLE);
                        mGridView.setVisibility(View.GONE);

                    }
                } else {
                    getFilteredList(Double.valueOf(mFilterDataModel.get(Integer.valueOf(mSelectedId)).getRangeA()), Double.valueOf(mFilterDataModel.get(Integer.valueOf(mSelectedId)).getRangeB()));

                }
                alert.dismiss();
                alert.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                alert.cancel();
            }
        });
        // Show the dialog
        alert.show();
    }


    //-----------------------Show Sort Dialog---------------------------//
    private void showSortDialog() {
        LayoutInflater lf = LayoutInflater.from(this);
        // This adds XML elements as a custom view (optional):
        final View customElementsView = lf.inflate(R.layout.dialog_sort, null);

        ListView filterListView = customElementsView.findViewById(R.id.listView);

        ArrayList<String> sortList = new ArrayList<String>();
        sortList.add("High To Low");
        sortList.add("Low To High");
        sortList.add("Newest First");
        sortList.add("Oldest First");

        SortListViewAdapter sortListViewAdapter = new SortListViewAdapter(ProductList.this, sortList, this);
        filterListView.setAdapter(sortListViewAdapter);

        Button button = customElementsView.findViewById(R.id.ok_Button);
        Button cancel = customElementsView.findViewById(R.id.cancel);

        mAlert = new AlertDialog.Builder(this)
                // This adds the custom view to the Dialog (optional):
                .setView(customElementsView)
                .setTitle(null)
                .setMessage(null)
                .create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlert.dismiss();
                mAlert.cancel();
            }
        });
//        filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ProductList.this, ""+position, Toast.LENGTH_SHORT).show();
//
//                getLocalSortedList(position);
//                alert.dismiss();
//                alert.cancel();
//                dismissPopup();
//            }
//        });
        // Show the dialog
        mAlert.show();
    }


    //-------------------GET FILTER LIST--------------------------//
    private void getFilteredList(Double start, Double end) {

        if (mGetSortedList != null) {
            mGetSortedList.clear();
        }

        for (int i = 0; i < mGetAllProductModels.size(); i++) {


            String priceStr;

            if (mGetAllProductModels.get(i).getOfferPrice() != null &&

                    !mGetAllProductModels.get(i).getOfferPrice().equalsIgnoreCase("null")) {
                priceStr = mGetAllProductModels.get(i).getOfferPrice();
            } else {
                priceStr = mGetAllProductModels.get(i).getPrice();
            }

            if (Double.valueOf(priceStr) > start && Double.valueOf(priceStr) < end) {

                mGetSortedList.add(mGetAllProductModels.get(i));

            }

        }

        if (mGetSortedList != null && mGetSortedList.size() > 0) {
            mlinearLayout.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridViewAdapter.update(mGetSortedList);

        } else {
            mlinearLayout.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);

        }

    }


    //-------------------GET PRICE LIST--------------------------//

    private void getPriceList() {
        if (mFilterDataModel != null) {
            mFilterDataModel.clear();
        }
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(ProductList.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                PRICE_RANGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    FilterDataModel filterDataModel = null;

                    JSONArray jsonArray = jsonObject.getJSONArray("Price_range");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        filterDataModel = new FilterDataModel();

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("Price");
                        filterDataModel.setId(jsonObject2.getString("id"));
                        filterDataModel.setRangeA(jsonObject2.getString("range_a"));
                        filterDataModel.setRangeB(jsonObject2.getString("range_b"));
                        mFilterDataModel.add(filterDataModel);
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
                Toast.makeText(ProductList.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
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


    @Override
    public void update(String val, String id, int check) {
        mVal = Integer.valueOf(val);
        if (!val.equalsIgnoreCase("-1")) {
            mSelectedId = id;
        } else {
            mSelectedId = "";
        }
        mFilterListViewAdapter.updateValue(Integer.valueOf(val), mFilterDataModel);
    }

    @Override
    public void unCheck(int val) {
        mSelectedId = "";
        mVal = val;
    }

    //---------------------------------- Show PopUp Dialog --------------------------------------//

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(this);

        ArrayList<String> sortList = new ArrayList<String>();
        sortList.add("High To Low");
        sortList.add("Low To High");
        sortList.add("Newest First");
        sortList.add("Oldest First");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.pop_up,
                sortList);
        // the drop down list is a list view
        ListView listViewSort = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener(onItemClickListener());

        // some other visual settings for popup window
        popupWindow.setFocusable(true);

        int width = new Util().getWidth(ProductList.this);
        if (width <= 0) {
            popupWindow.setWidth(500);
        } else {
            popupWindow.setWidth(width / 2);
        }
        popupWindow.setHeight(600);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_background));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // set the listview as popup content
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                //  getSortedList(String.valueOf(position));

                getLocalSortedList(position);

                dismissPopup();
            }
        };
    }

    private void getLocalSortedList(int position) {

        switch (position) {
            case 1:
                //-----------------------HIGH TO LOW------------------------//
                if (mSortedGetAllProductModels != null) {
                    mSortedGetAllProductModels.clear();
                }
                mSortedGetAllProductModels.addAll(mGetAllProductModels);


                Collections.sort(mSortedGetAllProductModels, new MyHighToLowPrice());
                mGridViewAdapter.update(mSortedGetAllProductModels);

                break;
            //-----------------------LOW TO HIGH------------------------//

            case 0:
                if (mSortedGetAllProductModels != null) {
                    mSortedGetAllProductModels.clear();
                }
                mSortedGetAllProductModels.addAll(mGetAllProductModels);


                Collections.sort(mSortedGetAllProductModels, new MyLowToHighPrice());
                mGridViewAdapter.update(mSortedGetAllProductModels);

                break;

            case 2:
                //-----------------------NEWEST FIRST-------------------------------------//
                mGridViewAdapter.update(mGetAllProductModels);

                break;
            case 3:
                //-----------------------OLDEST FIRST-------------------------------------//

                if (mSortedGetAllProductModels != null) {
                    mSortedGetAllProductModels.clear();
                }
                mSortedGetAllProductModels.addAll(mGetAllProductModels);


                Collections.sort(mSortedGetAllProductModels, new MySortedList());
                mGridViewAdapter.update(mSortedGetAllProductModels);


                break;
        }
    }


    //---------------------------------- Dismiss PopUp Dialog --------------------------------------//

    private void dismissPopup() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    @Override
    public void value(int position) {
        getLocalSortedList(position);
        mAlert.dismiss();
        mAlert.cancel();
    }

    class MySortedList implements Comparator<GetAllProductModel> {


        @Override
        public int compare(GetAllProductModel o1, GetAllProductModel o2) {
            if (Integer.valueOf(o1.getId()) < Integer.valueOf(o2.getId())) {
                return 1;
            } else {
                return -1;
            }
        }
    }


    class MyHighToLowPrice implements Comparator<GetAllProductModel> {


        @Override
        public int compare(GetAllProductModel o1, GetAllProductModel o2) {
            if (Integer.valueOf(o1.getPrice()) > Integer.valueOf(o2.getPrice())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    class MyLowToHighPrice implements Comparator<GetAllProductModel> {


        @Override
        public int compare(GetAllProductModel o1, GetAllProductModel o2) {

            Double val1, val2;

            if (o1.getOfferPrice() == null && o1.getOfferPrice().equalsIgnoreCase("null")) {
                val1 = Double.valueOf(o1.getOfferPrice());
            } else {
                val1 = Double.valueOf(o1.getPrice());

            }

            if (o2.getOfferPrice() == null && o2.getOfferPrice().equalsIgnoreCase("null")) {
                val2 = Double.valueOf(o2.getOfferPrice());
            } else {
                val2 = Double.valueOf(o2.getPrice());

            }

            if (val1 < val2) {
                return 1;
            } else {
                return -1;
            }
        }
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ProductList.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ProductList.this, "" + error, Toast.LENGTH_SHORT).show();
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
