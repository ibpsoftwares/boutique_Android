package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
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
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.activities.Productdetails;

import java.util.ArrayList;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;


/**
 * Created by apple on 21/02/18.
 */

public class WishListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CartViewModel> mWishList;
    private WishListInterface mListener;
    private SharedPreferences mSharedPreference;

    public WishListAdapter(Context context, ArrayList<CartViewModel> wishList, WishListInterface activity) {

        mWishList = new ArrayList<>();
        mContext = context;
        mWishList.addAll(wishList);
        mListener = activity;
        mSharedPreference= context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

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

            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol,""))+" "+mWishList.get(position).getOfferPrice());
        } else {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol,""))+" "+mWishList.get(position).getPrice());
        }
        ImageView delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateWishList(mWishList.get(position).getClothId(), position, mWishList);
            }
        });

        moveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.moveToWishList(mWishList.get(position).getClothId(), position, mWishList);

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Productdetails.class);

                i.putExtra("id", mWishList.get(position).getClothId());
                i.putExtra("cat_id", mWishList.get(position).getCategoryId());
                i.putExtra("cart_size", String.valueOf(((MainActivity)mContext).mCartCount));

                mContext.startActivity(i);
            }
        });

        return convertView;
    }

    public void UpdateData(ArrayList<CartViewModel> wishList) {
        if (mWishList != null) {
            mWishList.clear();
        }
        mWishList.addAll(wishList);
        notifyDataSetChanged();
    }
}
