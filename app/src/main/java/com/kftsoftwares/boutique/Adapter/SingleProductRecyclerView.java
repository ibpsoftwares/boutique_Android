package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.boutique.Interface.RelativeProductClick;
import com.kftsoftwares.boutique.Models.Banner;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.ProductList;

import java.util.ArrayList;


/**
 * Created by apple on 26/04/18.
 */

public class SingleProductRecyclerView extends RecyclerView.Adapter<SingleProductRecyclerView.MyViewHolder> {

    private Context mContext;
    ArrayList<Banner> mArrayList;
    RelativeProductClick mListener;

    public SingleProductRecyclerView(Context context, ArrayList<Banner> arrayList,
                                     RelativeProductClick relativeProductClick) {
        mContext = context;
        mArrayList = arrayList;
        mListener = relativeProductClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_product, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(mContext).load(mArrayList.get(position).getImage())
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
                        holder.productImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.productImage);
        holder.productImage.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.mTitle.setText(mArrayList.get(position).getName());

        if (mArrayList.get(position).getStock_size().equalsIgnoreCase("0")) {
            holder.outOfStock.setVisibility(View.VISIBLE);
        } else {
            holder.outOfStock.setVisibility(View.GONE);
        }

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.productData(mArrayList.get(position).getId(), mArrayList.get(position).getCategory_id());
            }
        });
        //  holder.productImage.setImageResource(images[position]);
    }

    public void update(ArrayList<Banner> arrayList) {
        if (mArrayList != null) {
            mArrayList.clear();
        }
        mArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mDescription;
        private ImageView productImage, outOfStock;

        public MyViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            mTitle = itemView.findViewById(R.id.name);
            outOfStock = itemView.findViewById(R.id.outOfStock);
        }
    }

}
