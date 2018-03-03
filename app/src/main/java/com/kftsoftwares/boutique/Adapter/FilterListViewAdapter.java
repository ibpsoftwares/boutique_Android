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

import com.kftsoftwares.boutique.Interface.FilterListView;
import com.kftsoftwares.boutique.Models.FilterDataModel;
import com.kftsoftwares.boutique.R;

import java.util.ArrayList;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;

/**
 * Created by apple on 21/02/18.
 */

public class FilterListViewAdapter extends BaseAdapter {
    private Integer selected_position = -1;
    private Context mContext;
    private ArrayList<FilterDataModel> mFilterDataModel;
    private int mPosition;
    private FilterListView mListener;
    private SharedPreferences sharedPreferences;

    public FilterListViewAdapter(Context context, ArrayList<FilterDataModel> modelArrayList, int val, FilterListView listener) {

        mContext = context;
        mFilterDataModel = modelArrayList;
        mListener = listener;
        mPosition = val;
        sharedPreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    public int getCount() {
        return mFilterDataModel.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.filter_adapter, null);
        }
        final CheckBox chkbox = convertView.findViewById(R.id.chkbox);
        TextView textView = convertView.findViewById(R.id.price);

        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mListener.update(mFilterDataModel.get(position).getId(), String.valueOf(position), 0);

                } else {

                    if (mPosition == Integer.valueOf(mFilterDataModel.get(position).getId())) {
                        mListener.unCheck(-1);
                    }
                }
            }
        });

        if (mPosition == Integer.valueOf(mFilterDataModel.get(position).getId())) {
            chkbox.setChecked(true);
        } else {
            chkbox.setChecked(false);
        }
        textView.setText("Price: " + mFilterDataModel.get(position).getRangeA().toString() + " To " + mFilterDataModel.get(position).getRangeB().toString());

        return convertView;
    }

    public void updateValue(int value, ArrayList<FilterDataModel> listView) {
        if (mFilterDataModel != null) {
            mFilterDataModel.clear();
        }
        mPosition = value;
        mFilterDataModel.addAll(listView);

        notifyDataSetChanged();
    }
}
