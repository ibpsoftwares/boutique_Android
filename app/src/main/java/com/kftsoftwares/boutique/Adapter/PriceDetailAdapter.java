package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.ProductList;

import java.util.ArrayList;

/**
 * Created by apple on 21/02/18.
 */

public class PriceDetailAdapter extends BaseAdapter {

    private Context mContext;


    public PriceDetailAdapter(Context context) {

        mContext = context;

    }

    @Override
    public int getCount() {
        return 1;
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

        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.price_detail_adapter,null);

        }


        return convertView;
    }
}
