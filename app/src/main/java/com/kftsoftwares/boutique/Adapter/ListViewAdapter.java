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

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mCategoryName,mCategoryId;

    public ListViewAdapter(Context context, ArrayList<String> categoryName, ArrayList<String> categoryId) {

        mContext = context;
        mCategoryName = categoryName;
        mCategoryId = categoryId;
    }

    @Override
    public int getCount() {
        return mCategoryName.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listadapter,null);

        }
        TextView textView = convertView.findViewById(R.id.categoryName);

        textView.setText(mCategoryName.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductList.class);
                i.putExtra("id",mCategoryId.get(position));
                mContext.startActivity(i);
            }
        });

        return convertView;
    }
}
