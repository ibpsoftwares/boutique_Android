package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Phone;
import static com.kftsoftwares.boutique.utils.Constants.SIGN_UP;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.User_ID_FOR_UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUserName, mEmail, mPassword, mMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
     //   getWindow().setBackgroundDrawableResource(R.drawable.background_green);

        TextView alreadyHaveAnAccount = findViewById(R.id.alreadyHaveAnAccount);
        Button signUp = findViewById(R.id.signUp);
        ImageView imageView = findViewById(R.id.backImage);
        mUserName = findViewById(R.id.input_username);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mMobileNumber = findViewById(R.id.input_mobile_number);

        alreadyHaveAnAccount.setOnClickListener(this);
        imageView.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alreadyHaveAnAccount:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.backImage:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.signUp:

                if (mUserName != null && mUserName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (mEmail != null && mEmail.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();

                } else if (mEmail != null && !Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    Toast.makeText(this, "Please enter correctEmail", Toast.LENGTH_SHORT).show();

                } else if (mPassword != null && mPassword.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

                } else if (mMobileNumber != null && mMobileNumber.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();

                }
                else {
                    signUpUser();

                }

                break;

        }

    }

    //----------------------------SIGN UP API--------------------//

    private void signUpUser() {


        final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                SIGN_UP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("status").equals("1")) {

                        mUserName.setText("");
                        mEmail.setText("");
                        mPassword.setText("");
                        mMobileNumber.setText("");

                        JSONArray jsonArray = jsonObject.getJSONArray("user");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(User_Name, jsonObject1.getString("username"));
                        editor.putString(Email, jsonObject1.getString("email"));
                        editor.putString(User_ID, jsonObject1.getString("userid"));
                        editor.putString(Phone, jsonObject1.getString("contact"));
                        editor.putString(User_ID_FOR_UPDATE_PROFILE, jsonObject1.getString("userdetail_id"));
                        editor.commit();


                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignUpActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mUserName.getText().toString());
                params.put("email", mEmail.getText().toString());
                params.put("password", mPassword.getText().toString());
                params.put("contact", mMobileNumber.getText().toString());
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
