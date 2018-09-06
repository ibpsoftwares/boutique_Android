package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.MainActivity;
import com.kftsoftwares.boutique.activities.ProductList;

import java.util.ArrayList;

/**
 * Created by apple on 21/02/18.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mCategoryName,mCategoryId,mCategoryImage;
    int images[] = {R.drawable.cat_one,R.drawable.cat_three,R.drawable.cat_two,R.drawable.cat_five};

    public ListViewAdapter(Context context, ArrayList<String> categoryName, ArrayList<String> categoryId, ArrayList<String> categoryImage) {

        mContext = context;
        mCategoryName = categoryName;
        mCategoryId = categoryId;
        mCategoryImage = categoryImage;

    }

    @Override
    public int getCount() {

       return mCategoryName.size();

       //return 4;
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
        final ImageView imageView = convertView.findViewById(R.id.imageView);
       // imageView.setImageResource(images[position]);

        Glide.with(mContext).load(mCategoryImage.get(position))
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
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        textView.setText(mCategoryName.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductList.class);
                i.putExtra("id",mCategoryId.get(position));
                i.putExtra("cart_id",""+((MainActivity)mContext).mCartCount);
                mContext.startActivity(i);
            }
        });

        return convertView;
    }
}
