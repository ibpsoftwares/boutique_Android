package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.boutique.Models.CartViewModel;
import com.kftsoftwares.boutique.Models.GetAllProductModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.activities.ProductList;
import com.kftsoftwares.boutique.activities.Productdetails;
import com.kftsoftwares.boutique.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Locale;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

/**
 * Created by apple on 20/02/18.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<GetAllProductModel> mArrayList;
    private ArrayList<GetAllProductModel> mSortedList;
    private DatabaseHandler sqLiteOpenHelper;
    private SharedPreferences sharedPreferences;

    public GridViewAdapter(Context context, ArrayList<GetAllProductModel> arrayList) {
        mContext = context;
        mArrayList = arrayList;
        this.mSortedList = new ArrayList<GetAllProductModel>();
        this.mSortedList.addAll(arrayList);
        sqLiteOpenHelper = new DatabaseHandler(context);
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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


        if (convertView == null) {

                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_single, null);

        }

        final ImageView imageView = convertView.findViewById(R.id.imageHeart);
        final ImageView productImage = convertView.findViewById(R.id.productImage);
        final ImageView outOfStock = convertView.findViewById(R.id.outOfStock);
        final TextView name = convertView.findViewById(R.id.name);
        final TextView price = convertView.findViewById(R.id.price);
        final TextView title = convertView.findViewById(R.id.title);
        final TextView oldPrice = convertView.findViewById(R.id.oldPrice);
        final ProgressBar progressBar = convertView.findViewById(R.id.progrss_bar);

        if (mArrayList!=null && mArrayList.get(position).getOfferPrice() != null &&

                !mArrayList.get(position).getOfferPrice().equalsIgnoreCase("null")) {

            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            oldPrice.setVisibility(View.VISIBLE);
            String priceText= Html.fromHtml(
                    sharedPreferences.getString(Symbol,""))+" "+mArrayList.get(position).getOfferPrice();
            String oldPriceText= Html.fromHtml(
                    sharedPreferences.getString(Symbol,""))+" "+mArrayList.get(position).getPrice();

            oldPrice.setText(oldPriceText);
            price.setText(priceText);
        } else {
            oldPrice.setVisibility(View.GONE);
            String priceText= Html.fromHtml(sharedPreferences.getString(Symbol,""))+" "+mArrayList.get(position).getPrice();

            price.setText(priceText);
        }

        if (mArrayList!=null && mArrayList.get(position).getWish_list() != null && mArrayList.get(position).getWish_list().equalsIgnoreCase("1")) {
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.heart));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.heartborder));
        }
        if (mArrayList.get(position).getStock_size().equalsIgnoreCase("0"))
        {
            outOfStock.setVisibility(View.VISIBLE);
        }
        else {
            outOfStock.setVisibility(View.GONE);

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArrayList.get(position).getWish_list().equalsIgnoreCase("1")) {

                    mArrayList.get(position).setWish_list("0");
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.heartborder));

                    sqLiteOpenHelper.removeDataFromWishList(mArrayList.get(position).getId());
                    if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase(""))
                    {
                        if (mContext instanceof MainActivity) {
                            ((MainActivity) mContext).getLocalWishListData();
                        } else {
                        }
                    } else {
                        if (mContext instanceof MainActivity) {
                            ((MainActivity) mContext).deleteFromWishList(mArrayList.get(position).getId(), "home");
                        } else {
                            ((ProductList) mContext).deleteFromWishList(mArrayList.get(position).getId(), "home");
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    CartViewModel cartViewModel = new CartViewModel();
                    cartViewModel.setClothId(mArrayList.get(position).getId());
                    cartViewModel.setTitle(mArrayList.get(position).getTitle());
                    cartViewModel.setImage1(mArrayList.get(position).getImage1());
                    cartViewModel.setPrice(mArrayList.get(position).getPrice());
                    cartViewModel.setCategoryId(mArrayList.get(position).getCategoryId());
                    cartViewModel.setStock_size(mArrayList.get(position).getStock_size());
                    cartViewModel.setProduct_id(mArrayList.get(position).getId());
                    cartViewModel.setOnlyStockSizeForlocal("0");
                    cartViewModel.setSize("noData");
                    cartViewModel.setSize_id("");
                    cartViewModel.setCat("wishList");
                    cartViewModel.setCount("1");
                    sqLiteOpenHelper.add(cartViewModel);
                    mArrayList.get(position).setWish_list("1");
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.heart));
                    if (sharedPreferences.getString(User_ID, "").equalsIgnoreCase(""))

                    {
                        if (mContext instanceof MainActivity) {
                            ((MainActivity) mContext).getLocalWishListData();
                        } else {
                            //   ((ProductList) mContext).addToWishList(mArrayList.get(position).getId(), "home");

                        }
                    } else {
                        if (mContext instanceof MainActivity) {
                            ((MainActivity) mContext).addToWishList(mArrayList.get(position).getId(), "home");


                        } else {

                            ((ProductList) mContext).addToWishList(mArrayList.get(position).getId(), "home");

                        }
                    }

                    notifyDataSetChanged();

                }
            }

        });

       // name.setText(mArrayList.get(position).getBrandName());
        name.setText(mArrayList.get(position).getTitle());


        Glide.with(mContext).load(mArrayList.get(position).getImage1())
                .crossFade()
                .fitCenter()
                .placeholder(R.mipmap.placeholder)
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        productImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArrayList!=null && mArrayList.size()>0) {
                    Intent i = new Intent(mContext, Productdetails.class);

                    Log.e("datawhenclick",mArrayList.get(position).getId());
                    i.putExtra("id", mArrayList.get(position).getId());
                    i.putExtra("cat_id", mArrayList.get(position).getCategoryId());
                 i.putExtra("cart_size",String.valueOf(((ProductList)mContext).mCartCount));

                    mContext.startActivity(i);
                }
            }
        });

        return convertView;


    }

    public void update(ArrayList<GetAllProductModel> arrayList) {
        if (mArrayList != null) {
            mArrayList.clear();
        }

        if (mSortedList != null) {
            mSortedList.clear();
        }

        mArrayList.addAll(arrayList);
        mSortedList.addAll(arrayList);
        notifyDataSetChanged();
    }


    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mArrayList.clear();
        if (charText.length() == 0) {
            mArrayList.addAll(mSortedList);
        } else {
            for (GetAllProductModel wp : mSortedList) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || wp.getBrandName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
