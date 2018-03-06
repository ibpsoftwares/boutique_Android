package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;

import static com.kftsoftwares.boutique.utils.Constants.Email;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;

public class Profile_Fragment extends Fragment {
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_, container, false);
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        TextView profileName = view.findViewById(R.id.profile_Name);
        EditText name = view.findViewById(R.id.name);
        EditText email = view.findViewById(R.id.email);
        profileName.setText(sharedPreferences.getString(User_Name,""));
        name.setText(sharedPreferences.getString(User_Name,""));
        email.setText(sharedPreferences.getString(Email,""));

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
}
