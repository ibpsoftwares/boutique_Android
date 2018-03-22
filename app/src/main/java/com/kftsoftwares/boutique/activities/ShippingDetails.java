package com.kftsoftwares.boutique.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.kftsoftwares.boutique.R;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Util.COUNTRY;

public class ShippingDetails extends AppCompatActivity implements View.OnClickListener{

    private Spinner mSpinner;

    private EditText mName,mAddress,mShortAddress,mTownCity,mZipCode,mPhoneNumber,mEmail;

    private SharedPreferences mSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);
        mSpinner = findViewById(R.id.spinner);

        mName = findViewById(R.id.name);
        mAddress = findViewById(R.id.address);
        mShortAddress = findViewById(R.id.shortAddress);
        mTownCity = findViewById(R.id.townCity);
        mZipCode = findViewById(R.id.zipCode);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mEmail = findViewById(R.id.email);

        mSharedPreference = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.textview_layout,COUNTRY);

        mSpinner.setAdapter(arrayAdapter);

        mName.setText(mSharedPreference.getString(User_ID,""));

        mName.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mShortAddress.setOnClickListener(this);
        mTownCity.setOnClickListener(this);
        mZipCode.setOnClickListener(this);
        mPhoneNumber.setOnClickListener(this);
        mEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
