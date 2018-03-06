package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.CHANGE_PASSWORD;
import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class Change_Password extends AppCompatActivity {

    EditText mOldPassword, mNewPassword, mConfirmPassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        ImageView backButton = findViewById(R.id.backButton);
        EditText email = findViewById(R.id.email);
        email.setText(sharedPreferences.getString(Email, ""));
        mOldPassword = findViewById(R.id.oldPassword);
        mNewPassword = findViewById(R.id.newPassword);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOldPassword.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Change_Password.this, "Enter old password", Toast.LENGTH_SHORT).show();
                } else if (mNewPassword.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Change_Password.this, "Enter new password", Toast.LENGTH_SHORT).show();

                } else if (mConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Change_Password.this, "Enter confirm password", Toast.LENGTH_SHORT).show();

                } else if (!mNewPassword.getText().toString().equalsIgnoreCase(mConfirmPassword.getText().toString())) {
                    Toast.makeText(Change_Password.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                } else {

                    updatePassword();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //---------------------CHANGE PASSWORD API----------------------//
    private void updatePassword() {
        //user_id , old_password, password
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                CHANGE_PASSWORD + "/" + TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("message")) {
                        Toast.makeText(Change_Password.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
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
                Toast.makeText(Change_Password.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sharedPreferences.getString(User_ID, ""));
                params.put("password", mNewPassword.getText().toString());
                params.put("old_password", mOldPassword.getText().toString());
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
