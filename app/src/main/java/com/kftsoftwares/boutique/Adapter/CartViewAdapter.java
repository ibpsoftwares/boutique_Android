package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kftsoftwares.boutique.Interface.CartListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.Productdetails;

import java.util.ArrayList;


/**
 * Created by apple on 21/02/18.
 */

public class CartViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CartViewModel> mCartList;
    private CartListInterface mListener;
    private int mCount=1;

    public CartViewAdapter(Context context, ArrayList<CartViewModel> wishList, CartListInterface activity) {

        mContext = context;
        mCartList = wishList;
        mListener= activity;

    }

    @Override
    public int getCount() {
        return mCartList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.new_cartview,null);
        }
        ImageView circleImageView = convertView.findViewById(R.id.profile_image);
        TextView price = convertView.findViewById(R.id.price);
        TextView name = convertView.findViewById(R.id.name);
        final TextView qnt_count = convertView.findViewById(R.id.qnt_count);
        qnt_count.setText(mCartList.get(position).getCount());

        name.setText(mCartList.get(position).getTitle());
        if (mCartList.get(position).getOfferPrice() != null &&
                !mCartList.get(position).getOfferPrice().equalsIgnoreCase("null")) {
            price.setText(mCartList.get(position).getOfferPrice());
        } else {
            price.setText(mCartList.get(position).getPrice());
        }
        RelativeLayout minus = convertView.findViewById(R.id.minus);
        RelativeLayout add = convertView.findViewById(R.id.add);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(mCartList.get(position).getCount()) <= 1)
                {}
                else {

                    qnt_count.setText(mCartList.get(position).getCount());

                   int value =  Integer.valueOf(mCartList.get(position).getCount());
                    mCartList.get(position).setCount(String.valueOf(value));

                    value = value-1;
                    mCartList.get(position).setCount(String.valueOf(value));

                    mListener.updatePrice(mCartList);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qnt_count.setText(mCartList.get(position).getCount());

                int value =  Integer.valueOf(mCartList.get(position).getCount());
                value = value + 1;
                mCartList.get(position).setCount(String.valueOf(value));
                mListener.updatePrice(mCartList);
            }
        });

        Glide.with(mContext).load(mCartList.get(position).getImage1())
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);

        price.setText(mCartList.get(position).getPrice());
        ImageView delete= convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateCartList(mCartList.get(position).getClothId());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Productdetails.class);

                i.putExtra("id", mCartList.get(position).getClothId());

                mContext.startActivity(i);
            }
        });

        return convertView;
    }
}
