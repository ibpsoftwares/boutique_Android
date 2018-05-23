package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Models.Image;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.Dob;
import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.Gender;
import static com.kftsoftwares.boutique.utils.Constants.Image_Link;
import static com.kftsoftwares.boutique.utils.Constants.LOGIN;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Phone;
import static com.kftsoftwares.boutique.utils.Constants.SHIPPING_DETAIL;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.User_ID_FOR_UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;
import static com.kftsoftwares.boutique.utils.Util.COUNTRY;

public class ShippingDetails extends AppCompatActivity implements View.OnClickListener {

    private Spinner mSpinner;

    private EditText mName, mAddress, mLocality, mTownCity, mZipCode, mPhoneNumber, mState;

    private SharedPreferences mSharedPreference;

    private CheckBox mSaturday, mSunday;
    private Button mSubmit;
    private int mAddressSelectedValue = -1;
    private String mResponse = "", mResponse_Schedule = "";
    private JSONArray mJsonArray;

    private String mCountryName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("response_value") != null) {
                mResponse = bundle.getString("response_value");

            }
            if (bundle.getString("jsonData") != null) {
                mResponse_Schedule = bundle.getString("jsonData");

            }
        }



        mSpinner = findViewById(R.id.spinner);

        mName = findViewById(R.id.name);
        mAddress = findViewById(R.id.address);
        mLocality = findViewById(R.id.locality);
        mTownCity = findViewById(R.id.townCity);
        mZipCode = findViewById(R.id.zipCode);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mSaturday = findViewById(R.id.saturday);
        mSunday = findViewById(R.id.sunday);
        mState = findViewById(R.id.state);
        mSubmit = findViewById(R.id.next);
        final LinearLayout officeDetailLayout = findViewById(R.id.officeDetailLayout);
        final RadioButton home = findViewById(R.id.home);
        RadioButton office = findViewById(R.id.office);
        RadioGroup addressRadioGroup = findViewById(R.id.addressRadioGroup);
        addressRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mAddressSelectedValue = checkedId;
                if (home.isChecked()) {
                    officeDetailLayout.setVisibility(View.GONE);
                } else {
                    officeDetailLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSharedPreference = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.textview_layout, COUNTRY);

        mSpinner.setAdapter(arrayAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mCountryName = COUNTRY[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mName.setText(mSharedPreference.getString(User_Name, ""));

        mSubmit.setOnClickListener(this);
        if (!mResponse.equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(mResponse);
                for (int i = 0; i < jsonObject.length(); i++) {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!mResponse_Schedule.equalsIgnoreCase("")) {

            try {
                JSONObject jsonObject = new JSONObject(mResponse_Schedule);
                JSONArray jsonArray = jsonObject.getJSONArray("shippingDetails");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    mLocality.setText(jsonObject1.getString("locality"));
                    mAddress.setText(jsonObject1.getString("address"));
                    mTownCity.setText(jsonObject1.getString("city"));
                    mZipCode.setText(jsonObject1.getString("zip_code"));
                    mState.setText(jsonObject1.getString("state"));

                    if (jsonObject1.getString("addressType").equalsIgnoreCase("office")) {
                        office.setChecked(true);
                        officeDetailLayout.setVisibility(View.VISIBLE);
                    } else {
                        home.setChecked(true);
                        officeDetailLayout.setVisibility(View.GONE);
                    }

                    if (jsonObject1.getString("availability").equalsIgnoreCase("saturday,sunday")) {
                        mSaturday.setChecked(true);
                        mSunday.setChecked(true);
                    } else if (jsonObject1.getString("availability").equalsIgnoreCase("saturday")) {
                        mSaturday.setChecked(true);
                    } else if (jsonObject1.getString("availability").equalsIgnoreCase("sunday")) {
                        mSunday.setChecked(true);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View v) {
        if (mCountryName.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select country", Toast.LENGTH_SHORT).show();
        }else if (mName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        } else if (mAddress.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();

        } else if (mLocality.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter locality/town", Toast.LENGTH_SHORT).show();

        } else if (mTownCity.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter city/district", Toast.LENGTH_SHORT).show();

        } else if (mState.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter state", Toast.LENGTH_SHORT).show();

        } else if (mZipCode.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter zip code", Toast.LENGTH_SHORT).show();

        } else if (mPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show();

        } else if (mPhoneNumber.getText().toString().length() != 10) {
            Toast.makeText(this, "Enter correct phone number", Toast.LENGTH_SHORT).show();

        } else if (mAddressSelectedValue == -1 || mAddressSelectedValue == 0) {
            Toast.makeText(this, "select address type", Toast.LENGTH_SHORT).show();

        } else {
            shippingDetailApi();
        }

    }

    //---------------------SHIPPING API----------------------//
    private void shippingDetailApi() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                SHIPPING_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    Intent i = new Intent(ShippingDetails.this, PriceDetailScreen.class);

                    i.putExtra("response_value", mResponse);
                    i.putExtra("shipping_value", response);
                    startActivity(i);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(ShippingDetails.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", mSharedPreference.getString(User_ID, ""));
                params.put("locality", mLocality.getText().toString());
                params.put("address", mAddress.getText().toString());
                params.put("zip_code", mZipCode.getText().toString());
                params.put("username", mName.getText().toString());
                params.put("city", mTownCity.getText().toString());
                params.put("contact", mPhoneNumber.getText().toString());
                params.put("country", mCountryName);
                params.put("userdetail_id", mSharedPreference.getString(User_ID_FOR_UPDATE_PROFILE, ""));
                params.put("state", mState.getText().toString());

                if (mAddressSelectedValue == 1) {
                    params.put("addressType", "home");
                    params.put("availability", "");
                } else {
                    params.put("addressType", "office");

                    if (mSaturday.isChecked() && mSunday.isChecked()) {
                        params.put("availability", "saturday,sunday");

                    } else if (mSaturday.isChecked()) {
                        params.put("availability", "saturday");

                    } else if (mSunday.isChecked()) {
                        params.put("availability", "sunday");

                    }
                }
                return params;
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
}
