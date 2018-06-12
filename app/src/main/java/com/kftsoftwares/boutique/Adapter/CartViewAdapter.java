package com.kftsoftwares.boutique.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        mListener = activity;
        mSharedPreference = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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

       if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.new_cartview, null);
        }
        ImageView circleImageView = convertView.findViewById(R.id.profile_image);
        TextView price = convertView.findViewById(R.id.price);
        TextView name = convertView.findViewById(R.id.name);
        TextView size = convertView.findViewById(R.id.size);
        TextView qnt_txt = convertView.findViewById(R.id.qnt_txt);
        ImageView outOfStockText = convertView.findViewById(R.id.outOfStockText);
        RelativeLayout relativeLayout1 = convertView.findViewById(R.id.relativeLayout1);
        final TextView qnt_count = convertView.findViewById(R.id.qnt_count);
        qnt_count.setText(mCartList.get(position).getCount());

        name.setText(mCartList.get(position).getTitle());
        if (mCartList.get(position).getOfferPrice() != null &&
                !mCartList.get(position).getOfferPrice().equalsIgnoreCase("null")) {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol, "")) + " " + mCartList.get(position).getOfferPrice());

        } else {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol, "")) + " " + mCartList.get(position).getPrice());
        }
        ImageView minus = convertView.findViewById(R.id.minus);
        ImageView outOfStock = convertView.findViewById(R.id.outOfStock);

        if (mCartList.get(position).getStock_size().equalsIgnoreCase("0")) {
         //   outOfStock.setVisibility(View.VISIBLE);
            outOfStockText.setVisibility(View.VISIBLE);
            relativeLayout1.setVisibility(View.INVISIBLE);
           // size.setVisibility(View.GONE);
           // price.setVisibility(View.GONE);

            qnt_txt.setVisibility(View.GONE);

        } else {
        //    outOfStock.setVisibility(View.GONE);
            outOfStockText.setVisibility(View.GONE);
            relativeLayout1.setVisibility(View.VISIBLE);
            qnt_txt.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            size.setVisibility(View.VISIBLE);
        }

        ImageView add = convertView.findViewById(R.id.plus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(mCartList.get(position).getCount()) <= 1  ) {

                }else {

                    qnt_count.setText(mCartList.get(position).getCount());

                    int value = Integer.valueOf(mCartList.get(position).getCount());
                    mCartList.get(position).setCount(String.valueOf(value));

                    value = value - 1;
                    mCartList.get(position).setCount(String.valueOf(value));

                    mListener.updatePrice(mCartList);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count =Integer.valueOf(mCartList.get(position).getCount());
                int stockSize =Integer.valueOf(mCartList.get(position).getStock_size());

                if ( count == stockSize )
                {
                    Toast.makeText(mContext, "You can order maximum "+mCartList.get(position).getStock_size()+" items", Toast.LENGTH_SHORT).show();
                }
                else {

                    qnt_count.setText(mCartList.get(position).getCount());

                    int value = Integer.valueOf(mCartList.get(position).getCount());
                    value = value + 1;
                    mCartList.get(position).setCount(String.valueOf(value));
                    mListener.updatePrice(mCartList);
                }
            }
        });

        Glide.with(mContext).load(mCartList.get(position).getImage1())
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);

        size.setText("Size : " + mCartList.get(position).getSize());

        ImageView delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateCartList(mCartList.get(position).getClothId(), mCartList.get(position).getSize(), position, mCartList.get(position).getSize_id());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Productdetails.class);

                i.putExtra("id", mCartList.get(position).getClothId());
                i.putExtra("cat_id", mCartList.get(position).getCategoryId());
                i.putExtra("cart_size", String.valueOf(mCartList.size()));

                mContext.startActivity(i);
                ((Activity) mContext).finish();

            }
        });

       /* if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cart_view_adapter, null);
        }
        ImageView circleImageView = convertView.findViewById(R.id.profile_image);
        TextView price = convertView.findViewById(R.id.price);
        TextView name = convertView.findViewById(R.id.name);
        TextView size = convertView.findViewById(R.id.size);
        Spinner spinner = convertView.findViewById(R.id.spinner);
        name.setText(mCartList.get(position).getTitle());
        if (mCartList.get(position).getOfferPrice() != null &&
                !mCartList.get(position).getOfferPrice().equalsIgnoreCase("null")) {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol, "")) + " " + mCartList.get(position).getOfferPrice());

        } else {
            price.setText(Html.fromHtml(mSharedPreference.getString(Symbol, "")) + " " + mCartList.get(position).getPrice());
        }

        Glide.with(mContext).load(mCartList.get(position).getImage1())
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);

        size.setText(mCartList.get(position).getSize());

        ImageView delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateCartList(mCartList.get(position).getClothId(), mCartList.get(position).getSize(), position, mCartList.get(position).getSize_id());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Productdetails.class);

                i.putExtra("id", mCartList.get(position).getClothId());
                i.putExtra("cat_id", mCartList.get(position).getCategoryId());
                i.putExtra("cart_size", String.valueOf(mCartList.size()));

                mContext.startActivity(i);
                ((Activity) mContext).finish();

            }
        });

       // String [] count = new String[Integer.valueOf(mCartList.get(position).getStock_size())];
        ArrayList<String> string = new ArrayList<>();
        for (int i =1;i<Integer.valueOf(mCartList.get(position).getStock_size());i++)
        {
            string.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.spinner_text, string);

        spinner.setAdapter(adapter);*/

        return convertView;
    }

    public void updateData(ArrayList<CartViewModel> cartViewModels) {
        mCartList = new ArrayList<>();
        mCartList.addAll(cartViewModels);
        notifyDataSetChanged();
    }
}
