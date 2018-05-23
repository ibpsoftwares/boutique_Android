package com.kftsoftwares.boutique.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kftsoftwares.boutique.Adapter.AlbumsAdapter;
import com.kftsoftwares.boutique.Adapter.SettingAdapter;
import com.kftsoftwares.boutique.Models.GetAllProductModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.Change_Password;
import com.kftsoftwares.boutique.activities.History;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.activities.Profile_Activity;
import com.kftsoftwares.boutique.database.DatabaseHandler;
import com.kftsoftwares.boutique.utils.SpacesItemDecoration;

import java.util.ArrayList;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;


public class Setting_Fragment extends Fragment implements View.OnClickListener {



    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).mCartView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).mCartCountText.setVisibility(View.GONE);
        ((MainActivity) getActivity()).mHeaderText.setText("Account");


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
        LinearLayout history = view.findViewById(R.id.history);
        changePassword.setOnClickListener(this);
        profile.setOnClickListener(this);
        signOut.setOnClickListener(this);
        history.setOnClickListener(this);
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
               startActivity(new Intent(getActivity(),Profile_Activity.class));
                break;
                case R.id.history:
               startActivity(new Intent(getActivity(),History.class));
                break;

            case R.id.signOut:
                DatabaseHandler handler = new DatabaseHandler(getActivity());
                handler.DeleteAllData();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();

                break;
        }

    }
}
