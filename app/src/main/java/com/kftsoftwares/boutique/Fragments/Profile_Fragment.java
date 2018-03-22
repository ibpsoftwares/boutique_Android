package com.kftsoftwares.boutique.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.ADD_TO_CART;
import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Phone;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;
import static com.kftsoftwares.boutique.utils.Constants.User_ID_FOR_UPDATE_PROFILE;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;

public class Profile_Fragment extends Fragment implements View.OnClickListener{
    private SharedPreferences mSharedPreferences;
    private Spinner mGenderSpinner;
    private EditText mMobileNumber,mUserName;
    private Context mContext;
    private TextView mDataOfBirth;
    private String mServerDate="";
    private Button mUpdate;
    private LinearLayout mLinearLayoutDobAndGender;
    private String[] mGender={"Select Gender","Male","Female"};
    private Button mViewProfile,mEditProfile;
    private int mYear, mMonth, mDay , mSelectedPosition=-1;
    ImageButton mFloatButton;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity)getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_, container, false);
        mSharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        TextView profileName = view.findViewById(R.id.profile_Name);
        mUserName = view.findViewById(R.id.name);
        EditText email = view.findViewById(R.id.email);
        email.setEnabled(false);
        RelativeLayout edit_profile_layout = view.findViewById(R.id.edit_profile_layout);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        mDataOfBirth = view.findViewById(R.id.date_of_birth);
        mLinearLayoutDobAndGender = view.findViewById(R.id.linearLayoutDobAndGender);
        mUpdate = view.findViewById(R.id.update);
        mGenderSpinner = view.findViewById(R.id.genderSpinner);
        mViewProfile = view.findViewById(R.id.profile);
        mEditProfile = view.findViewById(R.id.editProfile);
        mFloatButton = view.findViewById(R.id.editButton);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.textview_layout,mGender);
        mGenderSpinner.setAdapter(arrayAdapter);

        profileName.setText(mSharedPreferences.getString(User_Name,""));
        mUserName.setText(mSharedPreferences.getString(User_Name,""));
        mMobileNumber.setText(mSharedPreferences.getString(Phone,""));
        email.setText(mSharedPreferences.getString(Email,""));
        mDataOfBirth.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        edit_profile_layout.setOnClickListener(this);
        mEditProfile.setOnClickListener(this);
        mViewProfile.setOnClickListener(this);
        mFloatButton.setOnClickListener(this);
        mViewProfile.setVisibility(View.GONE);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPosition=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMobileNumber.setEnabled(false);
        mUserName.setEnabled(false);
        mLinearLayoutDobAndGender.setEnabled(false);
        mGenderSpinner.setEnabled(false);
        mDataOfBirth.setEnabled(false);
        mUpdate.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).mCartView.setVisibility(View.GONE);
        ((MainActivity)getActivity()).mCartCountText.setVisibility(View.GONE);
        ((MainActivity)getActivity()).mHeaderText.setText("Profile");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).mCartView.setVisibility(View.VISIBLE);
        if (((MainActivity)getActivity()).mCartCount!=0)
        {
            ((MainActivity)getActivity()).mCartCountText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
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
                        } ,mYear, mMonth, mDay);
                datePickerDialog.show();

            break;
            case R.id.update:
                if (mUserName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter user name", Toast.LENGTH_SHORT).show();
                }
                else if (mMobileNumber.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please select mobile number", Toast.LENGTH_SHORT).show();
                }
                else if(mMobileNumber.getText().toString().length()!=10)
                {
                    Toast.makeText(mContext, "Please select correct mobile number", Toast.LENGTH_SHORT).show();
                }
                else if (mServerDate.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please select date", Toast.LENGTH_SHORT).show();
                }
                else if (mSelectedPosition==-1 || mSelectedPosition ==0)
                {
                    Toast.makeText(mContext, "Please select gender", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateDataToServer();
                }

                break;
            case R.id.edit_profile_layout:

              if (mUpdate.getVisibility() == View.GONE)
              {
                  mUpdate.setVisibility(View.VISIBLE);
                  mUserName.setEnabled(true);
                  mMobileNumber.setEnabled(true);
                  mGenderSpinner.setEnabled(true);
                  mDataOfBirth.setEnabled(true);
                  mLinearLayoutDobAndGender.setEnabled(true);
              }
              else {
                  mUpdate.setVisibility(View.GONE);
                  mUserName.setEnabled(false);
                  mMobileNumber.setEnabled(false);
                  mLinearLayoutDobAndGender.setEnabled(false);
                  mGenderSpinner.setEnabled(false);
                  mDataOfBirth.setEnabled(false);
                  mUpdate.setVisibility(View.GONE);
              }
              case R.id.profile:

                //  mViewProfile.setBackgroundColor(ContextCompat.getColor(mContext,R.color.tabLayout));
                //  mEditProfile.setBackgroundColor(ContextCompat.getColor(mContext,R.color.green));
                  mEditProfile.setVisibility(View.VISIBLE);
                  mViewProfile.setVisibility(View.GONE);

                  mUpdate.setVisibility(View.GONE);
                  mUserName.setEnabled(false);
                  mMobileNumber.setEnabled(false);
                  mLinearLayoutDobAndGender.setEnabled(false);
                  mGenderSpinner.setEnabled(false);
                  mDataOfBirth.setEnabled(false);
                  mUpdate.setVisibility(View.GONE);

                break;
            case R.id.editProfile:

            //    mViewProfile.setBackgroundColor(ContextCompat.getColor(mContext,R.color.green));
             //   mEditProfile.setBackgroundColor(ContextCompat.getColor(mContext,R.color.tabLayout));
                mEditProfile.setVisibility(View.GONE);
                mViewProfile.setVisibility(View.VISIBLE);

                mUpdate.setVisibility(View.VISIBLE);
                    mUserName.setEnabled(true);
                    mMobileNumber.setEnabled(true);
                    mGenderSpinner.setEnabled(true);
                    mDataOfBirth.setEnabled(true);
                    mLinearLayoutDobAndGender.setEnabled(true);

                break;
            case R.id.editButton:
                mUpdate.setVisibility(View.VISIBLE);
                mUserName.setEnabled(true);
                mMobileNumber.setEnabled(true);
                mGenderSpinner.setEnabled(true);
                mDataOfBirth.setEnabled(true);
                mLinearLayoutDobAndGender.setEnabled(true);
                mFloatButton.setVisibility(View.GONE);
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
                params.put("image","");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String changeDateFormat(String date) {

        String formatedDate=null;

        SimpleDateFormat input = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat serverOutPut = new SimpleDateFormat("yyyy-MM-dd",  Locale.ENGLISH);
        try {
            formatedDate = output.format(input.parse(date));
            mServerDate = serverOutPut.format(input.parse(date));
            // parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

}
