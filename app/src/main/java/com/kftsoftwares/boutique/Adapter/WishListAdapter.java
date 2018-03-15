package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kftsoftwares.boutique.Interface.WishListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.Models.WishListModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.Productdetails;

import java.util.ArrayList;


/**
 * Created by apple on 21/02/18.
 */

public class WishListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CartViewModel> mWishList;
    private WishListInterface mListener;

    public WishListAdapter(Context context, ArrayList<CartViewModel> wishList, WishListInterface activity) {

        mContext = context;
        mWishList = wishList;
        mListener = activity;
    }

    @Override
    public int getCount() {
        return mWishList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wishlist_adapter, null);

        }
        ImageView circleImageView = convertView.findViewById(R.id.profile_image);
        TextView name = convertView.findViewById(R.id.name);
        TextView price = convertView.findViewById(R.id.price);
        Button moveToCart = convertView.findViewById(R.id.move_to_cart);
        Glide.with(mContext).load(mWishList.get(position).getImage1())
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);

        name.setText(mWishList.get(position).getTitle());

        if (mWishList.get(position).getOfferPrice() != null &&

                !mWishList.get(position).getOfferPrice().equalsIgnoreCase("null")) {

            price.setText(mWishList.get(position).getOfferPrice());
        } else {
            price.setText(mWishList.get(position).getPrice());
        }
        price.setText(mWishList.get(position).getPrice());
        ImageView delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateWishList(mWishList.get(position).getClothId());
            }
        });

        moveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.moveToWishList(mWishList.get(position).getClothId());
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Productdetails.class);

                i.putExtra("id", mWishList.get(position).getClothId());

                mContext.startActivity(i);
            }
        });

        return convertView;
    }
}
