package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kftsoftwares.boutique.Interface.FilterListView;
import com.kftsoftwares.boutique.Interface.SortListview;
import com.kftsoftwares.boutique.Models.FilterDataModel;
import com.kftsoftwares.boutique.R;

import java.util.ArrayList;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;

/**
 * Created by apple on 21/02/18.
 */

public class SortListViewAdapter extends BaseAdapter {
    private Integer selected_position = -1;
    private Context mContext;
    private ArrayList<String> mDataModel;
    private int mPosition;
    private SharedPreferences sharedPreferences;
    private SortListview mSortInterfaceListener;

    public SortListViewAdapter(Context context, ArrayList<String> modelArrayList , SortListview sortListener) {

        mContext = context;
        mDataModel = modelArrayList;
        mSortInterfaceListener = sortListener;
       sharedPreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mDataModel.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sort_adapter, null);
        }

        TextView name = convertView.findViewById(R.id.name);
        name.setText(mDataModel.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
                mSortInterfaceListener.value(position);
            }
        });
        return convertView;
    }


}
