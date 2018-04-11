package com.kftsoftwares.boutique.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;
import com.kftsoftwares.boutique.volly.SingletonRequestQueue;
import com.kftsoftwares.boutique.volly.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.Dob;
import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.Gender;
import static com.kftsoftwares.boutique.utils.Constants.Image_Link;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Phone;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.User_ID_FOR_UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;

public class Profile_Activity extends Activity implements View.OnClickListener {
    private SharedPreferences mSharedPreferences;
    private Spinner mGenderSpinner;
    private EditText mMobileNumber, mUserName;
    private Context mContext;
    private TextView mDataOfBirth;
    private String mServerDate = "";
    private Button mUpdate;
    private ImageView mProfileImage;
    private String[] mGender = {"Select Gender", "Male", "Female"};
    private int mYear, mMonth, mDay, mSelectedPosition = -1;
    ImageButton mFloatButton;
    private boolean mSelected = false;
    ImageView mEdit_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_);
        mContext = Profile_Activity.this;
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        TextView profileName = findViewById(R.id.profile_Name);
        mUserName = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        email.setEnabled(false);
        RelativeLayout edit_profile_layout = findViewById(R.id.edit_profile_layout);
        RelativeLayout relativeLayoutBack = findViewById(R.id.relativeLayoutBack);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mDataOfBirth = findViewById(R.id.date_of_birth);
        mUpdate = findViewById(R.id.update);
        mGenderSpinner = findViewById(R.id.genderSpinner);
        mFloatButton = findViewById(R.id.editButton);
        mProfileImage = findViewById(R.id.profileImage);
        mEdit_image = findViewById(R.id.edit_image);




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.textview_layout, mGender);
        mGenderSpinner.setAdapter(arrayAdapter);

        profileName.setText(mSharedPreferences.getString(User_Name, ""));
        mUserName.setText(mSharedPreferences.getString(User_Name, ""));
        mMobileNumber.setText(mSharedPreferences.getString(Phone, ""));
        email.setText(mSharedPreferences.getString(Email, ""));

        if (!mSharedPreferences.getString(Dob,"").equalsIgnoreCase("null"))
        {
            mDataOfBirth.setText(mSharedPreferences.getString(Dob,""));
        }

        if (!mSharedPreferences.getString(Gender,"").equalsIgnoreCase("null"))
        {
            int pos = new ArrayList<String>(Arrays.asList(mGender)).indexOf(mSharedPreferences.getString(Gender,""));
            mGenderSpinner.setSelection(pos);
        }
        String str = mSharedPreferences.getString(Image_Link,"");

        if (!mSharedPreferences.getString(Image_Link,"").equalsIgnoreCase("null"))
        {
            String image = mSharedPreferences.getString(Image_Link,"");

            Glide.with(mContext).load(image)
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

                            mProfileImage.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mProfileImage);
        }


        mDataOfBirth.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        edit_profile_layout.setOnClickListener(this);
        mFloatButton.setOnClickListener(this);
        relativeLayoutBack.setOnClickListener(this);
        mEdit_image.setOnClickListener(this);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMobileNumber.setEnabled(false);
        mUserName.setEnabled(false);
        mGenderSpinner.setEnabled(false);
        mDataOfBirth.setEnabled(false);
        mEdit_image.setEnabled(false);
        mUpdate.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_of_birth:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mDataOfBirth.setText(changeDateFormat(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                break;
            case R.id.update:
                if (mUserName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please enter user name", Toast.LENGTH_SHORT).show();
                } else if (mMobileNumber.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please select mobile number", Toast.LENGTH_SHORT).show();
                } else if (mMobileNumber.getText().toString().length() != 10) {
                    Toast.makeText(mContext, "Please select correct mobile number", Toast.LENGTH_SHORT).show();
                } else if (mServerDate.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please select date", Toast.LENGTH_SHORT).show();
                } else if (mSelectedPosition == -1 || mSelectedPosition == 0) {
                    Toast.makeText(mContext, "Please select gender", Toast.LENGTH_SHORT).show();
                } else {
                    uploadBitmap();
                }

                break;
            case R.id.edit_profile_layout:

                break;
            case R.id.profile:


                break;
            case R.id.editProfile:


                break;
            case R.id.relativeLayoutBack:
                finish();
                break;
            case R.id.editButton:
                mUpdate.setVisibility(View.VISIBLE);
                mUserName.setEnabled(true);
                mMobileNumber.setEnabled(true);
                mGenderSpinner.setEnabled(true);
                mDataOfBirth.setEnabled(true);
                mEdit_image.setEnabled(true);
                mFloatButton.setVisibility(View.GONE);
                break;

            case R.id.edit_image:
                imageBrowse();
                break;
        }
    }

    private void updateDataToServer() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(mContext, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    mFloatButton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                params.put("id", mSharedPreferences.getString(User_ID, ""));
                params.put("userdetail_id", mSharedPreferences.getString(User_ID_FOR_UPDATE_PROFILE, ""));
                params.put("dob", mDataOfBirth.getText().toString());
                params.put("contact", mMobileNumber.getText().toString());
                params.put("username", mUserName.getText().toString());
                params.put("gender", mGender[mSelectedPosition]);
                params.put("image", "");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String changeDateFormat(String date) {

        String formatedDate = null;

        SimpleDateFormat input = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat serverOutPut = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            formatedDate = output.format(input.parse(date));
            mServerDate = serverOutPut.format(input.parse(date));
            // parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }


    //Image Upload Work

    private void imageBrowse() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Get the path from the Uri
                String path = getPathFromURI(selectedImageUri);
                Log.i("Image Path : ", "Image Path : " + path);
                // Set the image in ImageView
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File image = new File(sd+path);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                  //  bitmap1 = Bitmap.createScaledBitmap(bitmap1, mProfileImage.getWidth(),mProfileImage.getHeight(),true);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    mProfileImage.setImageURI(selectedImageUri);
                    mSelected = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void uploadBitmap() {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPDATE_PROFILE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("user");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    SharedPreferences.Editor editor = mSharedPreferences.edit();

                    editor.putString(Phone, jsonObject1.getString("contact"));
                    editor.putString(Gender, jsonObject1.getString("gender"));
                    editor.putString(Dob, jsonObject1.getString("dob"));
                    editor.putString(Image_Link, jsonObject1.getString("image"));

                    editor.commit();
                    Toast.makeText(mContext, ""+jsonObject.getString("message" ), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mUpdate.setVisibility(View.GONE);
                mUserName.setEnabled(false);
                mMobileNumber.setEnabled(false);
                mGenderSpinner.setEnabled(false);
                mDataOfBirth.setEnabled(false);
                mFloatButton.setVisibility(View.VISIBLE);
 //     textView.setText(resultResponse);
//                Toast.makeText(AfterLogin.this, resultResponse, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", mSharedPreferences.getString(User_ID, ""));
                params.put("userdetail_id", mSharedPreferences.getString(User_ID_FOR_UPDATE_PROFILE, ""));
                params.put("dob", mServerDate);
                params.put("contact", mMobileNumber.getText().toString());
                params.put("username", mUserName.getText().toString());
                params.put("gender", mGender[mSelectedPosition]);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer ZWNvbW1lcmNl");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (mSelected) {
                    params.put("image", new DataPart("" + mSharedPreferences.getString(User_ID, "") + "_" + mUserName.getText().toString() + ".jpg", AppController.getFileDataFromDrawable(mContext, mProfileImage.getDrawable()), "image/jpeg"));
                }
                return params;
            }

        };
        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(multipartRequest);
    }



}
