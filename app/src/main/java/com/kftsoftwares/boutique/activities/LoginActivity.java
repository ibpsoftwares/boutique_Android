package com.kftsoftwares.boutique.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.DEVICE_UD_ID;
import static com.kftsoftwares.boutique.utils.Constants.Dob;
import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.FORGET_PASSWORD;
import static com.kftsoftwares.boutique.utils.Constants.Gender;
import static com.kftsoftwares.boutique.utils.Constants.Image_Link;
import static com.kftsoftwares.boutique.utils.Constants.LOGIN;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Phone;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.UPLOAD_LOCAL_DATA;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.User_ID_FOR_UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmail, mPassword;
    private SharedPreferences sharedpreferences;
    private DatabaseHandler mDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        Button signUp = findViewById(R.id.signUp_button);
        Button signIn_button = findViewById(R.id.signIn_button);
        ImageView backButton = findViewById(R.id.backImage);
        TextView forgetPassword = findViewById(R.id.forget_password);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mDatabaseHandler = new DatabaseHandler(LoginActivity.this);

        signUp.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        backButton.setOnClickListener(this);
        signIn_button.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    }

    private void requestForPermission() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("PERMISSIONS REQUIRED");
                builder.setMessage("It allows us to establish identity of your device on the system so we can debug if you face any issue.The permission is called make & manage phone calls");
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                    }
                });
                builder.setNegativeButton("LATER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(DEVICE_UD_ID, m_androidId);
                        editor.apply();
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (sharedpreferences.getBoolean(Manifest.permission.READ_PHONE_STATE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("PERMISSIONS REQUIRED");
                builder.setMessage("It allows us to establish identity of your device on the system so we can debug if you face any issue.The permission is called make & manage phone calls");
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 101);
                        Toast.makeText(getBaseContext(), "Go to App Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("LATER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(DEVICE_UD_ID, m_androidId);
                        editor.apply();
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            }
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Manifest.permission.READ_PHONE_STATE, true);
            editor.apply();
        } else {
            //You already have the permission, just go ahead.
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String m_deviceId = TelephonyMgr.getDeviceId();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(DEVICE_UD_ID, m_deviceId);
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_button:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;

            case R.id.forget_password:
                showAlertDialog();
                break;

            case R.id.backImage:
                finish();
                break;

            case R.id.signIn_button:
                if (mEmail != null && mEmail.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (mEmail != null && !Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    Toast.makeText(this, "Please enter correct email", Toast.LENGTH_SHORT).show();

                } else if (mPassword != null && mPassword.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    loginApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101 && grantResults[0] != -1) {
            //You already have the permission, just go ahead.
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String m_deviceId = TelephonyMgr.getDeviceId();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(DEVICE_UD_ID, m_deviceId);
            editor.apply();
        } else {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED && sharedpreferences.getString(DEVICE_UD_ID, "").equalsIgnoreCase("")) {
                // requestForPermission();
            }
        }
    }


    //---------------------LOGIN API----------------------//
    private void loginApi() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("user");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(User_Name, jsonObject1.getString("username"));
                        editor.putString(Email, jsonObject1.getString("email"));
                        editor.putString(User_ID, jsonObject1.getString("userid"));
                        editor.putString(Phone, jsonObject1.getString("contact"));
                        editor.putString(Gender, jsonObject1.getString("gender"));
                        editor.putString(Dob, jsonObject1.getString("dob"));
                        editor.putString(Image_Link, jsonObject1.getString("image"));
                        editor.putString(User_ID_FOR_UPDATE_PROFILE, jsonObject1.getString("userdetail_id"));

                        editor.commit();
                        uploadTheLocalData(jsonObject1.getString("userid"));


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mEmail.getText().toString());
                params.put("password", mPassword.getText().toString());
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //---------------------ALERT DIALOG ON FORGET CLICK----------------------//
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Forget Password");
        //alertDialog.setMessage(null);

        final EditText input = new EditText(LoginActivity.this);
        input.setHint("Enter Email...");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches()) {

                            Toast.makeText(LoginActivity.this, "Please enter correct email", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "Need Api", Toast.LENGTH_SHORT).show();


                            //   sendEmail(input.getText().toString());
                        }

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    //--------------------- FORGET PASSWORD API----------------------//
    private void sendEmail(final String email) {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                FORGET_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //--------------Upload the local data--------------------------------//
    private void uploadTheLocalData(final String user_id) {
        String tag_string_req = "string_req";
        ArrayList<CartViewModel> cartID = new ArrayList<>();
        ArrayList<String> wishID = new ArrayList<>();


        wishID.addAll(mDatabaseHandler.getUser_ID_Data("wishList"));
        cartID.addAll(mDatabaseHandler.getAllDataOfCart());

        JSONObject cart_obj = null;
        final JSONArray jsonArrayCart = new JSONArray();
        for (int i = 0; i < cartID.size(); i++) {
            cart_obj = new JSONObject();
            try {
                cart_obj.put("cloth_id", cartID.get(i).getClothId());
                String str = cartID.get(i).getSize_id();
                cart_obj.put("size_id", cartID.get(i).getSize_id());


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArrayCart.put(cart_obj);
        }
        final JSONObject finalCartobject = new JSONObject();
        try {
            finalCartobject.put("cart", jsonArrayCart);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject wish_obj = null;
        final JSONArray jsonArrayWish = new JSONArray();
        for (int i = 0; i < wishID.size(); i++) {
            wish_obj = new JSONObject();
            try {
                wish_obj.put("cloth_id", wishID.get(i));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArrayWish.put(wish_obj);
        }
        final JSONObject finalWishobject = new JSONObject();
        try {
            finalWishobject.put("wishlist", jsonArrayWish);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StringRequest strReq = new StringRequest(Request.Method.POST,

                UPLOAD_LOCAL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                map.put("user_id", user_id);
                map.put("cart_id", jsonArrayCart.toString());
                map.put("wishlist_id", jsonArrayWish.toString());
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
