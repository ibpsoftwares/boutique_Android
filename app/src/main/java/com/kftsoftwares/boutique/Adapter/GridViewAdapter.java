package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kftsoftwares.boutique.Models.GetAllProductModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.ProductList;
import com.kftsoftwares.boutique.activities.Productdetails;

import java.util.ArrayList;

/**
 * Created by apple on 20/02/18.
 */

public class GridViewAdapter extends BaseAdapter {

   private Context mContext;

   private ArrayList<GetAllProductModel> mArrayList ;

    public GridViewAdapter(Context context ,ArrayList<GetAllProductModel> arrayList) {
        mContext = context;
        mArrayList =arrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_single,null);

        }
        final ImageView imageView = convertView.findViewById(R.id.imageHeart);
        final ImageView productImage = convertView.findViewById(R.id.productImage);
        final TextView name = convertView.findViewById(R.id.name);
        final TextView price = convertView.findViewById(R.id.price);
        final TextView title = convertView.findViewById(R.id.title);
        final TextView oldPrice = convertView.findViewById(R.id.oldPrice);

        oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      name.setText(mArrayList.get(position).getBrandName());
        price.setText(mArrayList.get(position).getPrice());
        title.setText(mArrayList.get(position).getTitle());
        final LinearLayout linearLayout = convertView.findViewById(R.id.linearLayout);


        Glide.with(mContext).load(mArrayList.get(position).getImage1())
                .crossFade()
                .fitCenter()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, Productdetails.class);
                i.putExtra("id",mArrayList.get(position).getId());
                mContext.startActivity(i);


            }
        });

        return convertView;



    }
}