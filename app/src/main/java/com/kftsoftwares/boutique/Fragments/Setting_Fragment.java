package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.Change_Password;
import com.kftsoftwares.boutique.activities.MainActivity;


public class Setting_Fragment extends Fragment implements View.OnClickListener{


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).mCartView.setVisibility(View.GONE);
        ((MainActivity)getActivity()).mCartCountText.setVisibility(View.GONE);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)getActivity()).mCartView.setVisibility(View.GONE);
        ((MainActivity)getActivity()).mCartCountText.setVisibility(View.GONE);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_, container, false);

        LinearLayout changePassword = view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.changePassword:
                startActivity(new Intent(getActivity(), Change_Password.class));

                break;
        }

    }
}
