package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
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
        final TextView qnt_count = convertView.findViewById(R.id.qnt_count);
        RelativeLayout minus = convertView.findViewById(R.id.minus);
        RelativeLayout add = convertView.findViewById(R.id.add);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCount == 1)
                {  }
                else {
                    mCount--;
                    qnt_count.setText(String.valueOf(mCount));
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount++;
                qnt_count.setText(String.valueOf(mCount));
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


        return convertView;
    }
}
