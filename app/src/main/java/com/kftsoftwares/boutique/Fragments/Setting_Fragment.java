package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.Change_Password;
import com.kftsoftwares.boutique.activities.LoginActivity;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.activities.SplashScreen;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;


public class Setting_Fragment extends Fragment implements View.OnClickListener {


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).mCartView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).mCartCountText.setVisibility(View.GONE);
        ((MainActivity)getActivity()).mHeaderText.setText("Setting");



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).mCartView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).mCartCountText.setVisibility(View.GONE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_, container, false);

        LinearLayout changePassword = view.findViewById(R.id.changePassword);
        LinearLayout signOut = view.findViewById(R.id.signOut);
        LinearLayout profile = view.findViewById(R.id.profile);
        changePassword.setOnClickListener(this);
        profile.setOnClickListener(this);
        signOut.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.changePassword:
             startActivity(new Intent(getActivity(), Change_Password.class));
                //Toast.makeText(getActivity(), "On Working", Toast.LENGTH_SHORT).show();
                break;
                case R.id.profile:
                    Fragment profile = new Profile_Fragment();

                    ((MainActivity)getActivity()).changeFragment(profile, "Profile",2);
                break;

            case R.id.signOut:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(getActivity(), SplashScreen.class));

                break;
        }

    }
}
