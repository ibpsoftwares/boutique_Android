

package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.PriceDetailAdapter;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.CANCEL_CHECKOUT;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.ORDER_DETAIL;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class PriceDetailScreen extends AppCompatActivity {

    private String mResponse = "";
    private String mShippingValue = "";
    private String mCheckOutdata = "";

    private int mTotalCount;
    private int mPaymentCategory = -1;
    private int mDeliveryCharges;
    private int mTotalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_detail_screen);

        ListView listView = findViewById(R.id.listView);
        TextView totalCount = findViewById(R.id.itemCount);
        TextView totalAmount = findViewById(R.id.orderTotalAmount);
        TextView deliveryAmount = findViewById(R.id.deliveryAmount);
        TextView totalPayableAmount = findViewById(R.id.totalPayableAmount);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   cancelCheckOut();
                finish();

            }
        });

        final RadioButton creditCard = findViewById(R.id.creditCard);
        RadioButton cashOnDelivery = findViewById(R.id.cashOnDelivery);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (creditCard.isChecked()) {
                    mPaymentCategory = 1;
                } else {
                    mPaymentCategory = 2;

                }

            }
        });

        Button confirmOrder = findViewById(R.id.confirmOrder);
        Button cancel = findViewById(R.id.cancel);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("response_value") != null) {

                mResponse = bundle.getString("response_value");
            }
            if (bundle.getString("shipping_value") != null) {

                mShippingValue = bundle.getString("shipping_value");

            }   if (bundle.getString("check_outData") != null) {

                mCheckOutdata = bundle.getString("check_outData");
            }
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

               // cancelCheckOut();

            }
        });

        if (!mResponse.equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(mResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("priceDetail");
                JSONArray jsonArrayForShipping = jsonObject.getJSONArray("shippingDetails");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    mTotalCount = mTotalCount + Integer.valueOf(jsonObject1.getString("quantity"));
                    mTotalAmount = mTotalAmount + Integer.valueOf(jsonObject1.getString("totalPrice"));
                }

                mDeliveryCharges = Integer.valueOf(jsonObject.getString("deliveryCharges"));
                if (jsonArrayForShipping.length() > 0) {
                    PriceDetailAdapter priceDetailAdapter = new PriceDetailAdapter(this, jsonArrayForShipping, mResponse);

                    listView.setAdapter(priceDetailAdapter);
                }
                totalCount.setText(String.valueOf(mTotalCount).concat(" ITEMS"));
                totalAmount.setText(String.valueOf(mTotalAmount));
                deliveryAmount.setText(String.valueOf(mDeliveryCharges));
                totalPayableAmount.setText(String.valueOf(mTotalAmount + mDeliveryCharges));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!mShippingValue.equalsIgnoreCase("")) {

            try {
                JSONObject jsonObject = new JSONObject(mShippingValue);
                JSONArray jsonObject1 = jsonObject.getJSONArray("shippingDetails");

                PriceDetailAdapter priceDetailAdapter = new PriceDetailAdapter(this, jsonObject1, mResponse);

                listView.setAdapter(priceDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPaymentCategory == -1 || mPaymentCategory == 0) {

                    Toast.makeText(PriceDetailScreen.this, "Please select the payment type", Toast.LENGTH_SHORT).show();

                } else if (mPaymentCategory == 2) {

                    checkOrder();
                } else {

                    Intent i = new Intent(PriceDetailScreen.this, Payment.class);
                    i.putExtra("amount", String.valueOf(mTotalAmount + mDeliveryCharges));
                    i.putExtra("check_outData", mCheckOutdata);
                    startActivity(i);
                    finish();
                }
            }
        });

    }


    private void checkOrder() {
        String tag_string_req = "string_req";

        final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        final ProgressDialog pDialog = new ProgressDialog(PriceDetailScreen.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                ORDER_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    showDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(PriceDetailScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                map.put("paymentType", "cod");
                map.put("amount", String.valueOf(mTotalAmount));
                map.put("user_id", sharedPreferences.getString(User_ID,""));
                map.put("cartArray", mCheckOutdata);

                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        LayoutInflater lf = LayoutInflater.from(this);
        // This adds XML elements as a custom view (optional):
        final View customElementsView = lf.inflate(R.layout.success_order_detail, null);


        Button button = customElementsView.findViewById(R.id.ok_Button);

        final AlertDialog alert = new AlertDialog.Builder(this)
                // This adds the custom view to the Dialog (optional):
                .setView(customElementsView)
                .setTitle(null)
                .setMessage(null)
                .setCancelable(false)
                .create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                alert.cancel();
                Intent i = new Intent(PriceDetailScreen.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        // Show the dialog
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  cancelCheckOut();
    }
}
