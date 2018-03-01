package com.kftsoftwares.boutique.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;

public class Profile_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
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
}
