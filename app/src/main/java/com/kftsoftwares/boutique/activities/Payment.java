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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.ORDER_DETAIL;
import static com.kftsoftwares.boutique.utils.Constants.PAYMENT;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class Payment extends AppCompatActivity {

    private EditText mCardNumber, mMonth, mYear, mCvv, mEmail;
    private String mAmount, mCheckOutData = "";
    private TextView mAmountTextView;
    private Button mNext;
    private ProgressDialog pDialog;

    private ImageView mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mCardNumber = findViewById(R.id.cartNumber);
        mMonth = findViewById(R.id.month);
        mYear = findViewById(R.id.year);
        mCvv = findViewById(R.id.cvvNumber);
        mEmail = findViewById(R.id.email);
        mAmountTextView = findViewById(R.id.amount);
        mBackButton = findViewById(R.id.backButton);

        mCardNumber.setText("4242424242424242");
        mMonth.setText("12");
        mYear.setText("2019");
        mCvv.setText("123");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        mNext = findViewById(R.id.next);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        mEmail.setText(sharedPreferences.getString(Email, ""));

        Bundle b = getIntent().getExtras();

        if (b != null) {
            if (b.getString("amount") != null) {
                mAmount = b.getString("amount");
                mAmountTextView.setText(mAmount);
            }

            if (b.getString("check_outData") != null) {
                mCheckOutData = b.getString("check_outData");
            }

        }
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.show();
                Card card = new Card(mCardNumber.getText().toString(), Integer.valueOf(mMonth.getText().toString()), Integer.valueOf(mYear.getText().toString()), mCvv.getText().toString());
// Remember to validate the card object before you use it to save time.
                if (!card.validateNumber()) {
                    // Do not continue token creation.
                    Toast.makeText(Payment.this, "Not Valid", Toast.LENGTH_SHORT).show();
                } else {
                    Stripe stripe = new Stripe(Payment.this, "pk_test_zUAsoiGmAgvLpzYruFVCr5Ej");
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
                                    String token1 = "" + token.getId();
                                    //  sendDataForPayment(token1,mAmount);
                                    checkOrder(token1, mAmount);
                                }

                                public void onError(Exception error) {
                                    // Show localized error message
                                    Toast.makeText(Payment.this, "" + error, Toast.LENGTH_SHORT).show();


                                }
                            }
                    );

                }
            }
        });


    }

    private void sendDataForPayment(final String token, final String amount) {

        String tag_string_req = "string_req";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                PAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    if (response != null) {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonArray = jsonObject.getJSONObject("customer_array");
                        String status = jsonArray.getString("status");

                        if (status.equalsIgnoreCase("succeeded")) {
                            //  checkOrder();
                        } else {

                            showFailedDialog();
                        }

                    } else {
                        showFailedDialog();

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
                Toast.makeText(Payment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amount", amount);
                params.put("token", token);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void checkOrder(final String token, final String amount) {
        String tag_string_req = "string_req";

        final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        final ProgressDialog pDialog = new ProgressDialog(Payment.this);
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
                    if (response != null) {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonArray = jsonObject.getJSONObject("customer_array");
                        String status = jsonArray.getString("status");

                        if (status.equalsIgnoreCase("succeeded")) {
                            showDialog();
                        } else {

                            showFailedDialog();
                        }

                    } else {
                        showFailedDialog();

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
                Toast.makeText(Payment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                map.put("paymentType", "card");
                map.put("amount", mAmount);
                map.put("user_id", sharedPreferences.getString(User_ID, ""));
                map.put("cartArray", mCheckOutData);
                map.put("token", token);
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
                Intent i = new Intent(Payment.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        // Show the dialog
        alert.show();
    }

    private void showFailedDialog() {
        LayoutInflater lf = LayoutInflater.from(this);
        // This adds XML elements as a custom view (optional):
        final View customElementsView = lf.inflate(R.layout.failed_order_detail, null);


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
                Intent i = new Intent(Payment.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        // Show the dialog
        alert.show();
    }
}
