package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kftsoftwares.boutique.Interface.CartListInterface;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.Productdetails;

import java.util.ArrayList;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;


/**
 * Created by apple on 21/02/18.
 */

public class CartViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CartViewModel> mCartList;
    private CartListInterface mListener;
    private int mCount = 1;
    private SharedPreferences mSharedPreference;

    public CartViewAdapter(Context context, ArrayList<CartViewModel> wishList, CartListInterface activity) {

        mContext = context;
        mCartList = wishList;
        mListener= activity;
        mSharedPreference= context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

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
        TextView size = convertView.findViewById(R.id.size);
        final TextView qnt_count = convertView.findViewById(R.id.qnt_count);
        qnt_count.setText(mCartList.get(position).getCount());

        name.setText(mCartList.get(position).getTitle());
        if (mCartList.get(position).getOfferPrice() != null &&
                !mCartList.get(position).getOfferPrice().equalsIgnoreCase("null")) {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol,""))+" "+mCartList.get(position).getOfferPrice());

        } else {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol,""))+" "+mCartList.get(position).getPrice());
        }
        ImageView minus = convertView.findViewById(R.id.minus);
        ImageView add = convertView.findViewById(R.id.plus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(mCartList.get(position).getCount()) <= 1)
                {

                }
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

        size.setText("Size : "+ mCartList.get(position).getSize());
        ImageView delete= convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateCartList(mCartList.get(position).getClothId(),mCartList.get(position).getSize(), position,mCartList.get(position).getSize_id());
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

    public void updateData(ArrayList<CartViewModel> cartViewModels)
    {
        if (mCartList!=null)
        {
            mCartList.clear();
        }
        mCartList.addAll(cartViewModels);
        notifyDataSetChanged();
    }
}
