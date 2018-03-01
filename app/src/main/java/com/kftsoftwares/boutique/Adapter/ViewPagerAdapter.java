package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.boutique.R;

import java.util.ArrayList;

/**
 * Created by apple on 20/02/18.
 */

public class ViewPagerAdapter extends PagerAdapter {

     private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<String> mArrayList;

    public ViewPagerAdapter(Context context,ArrayList<String> arr) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mArrayList =  arr;
    }

    @Override
    public int getCount() {
        return mArrayList.size();

    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

       ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
         final ProgressBar progressBar = itemView.findViewById(R.id.progress_Bar);
      // imageView.setImageResource(mImages[position]);
        Glide.with(mContext).load(mArrayList.get(position))
                .crossFade()
                .fitCenter()
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    public void update(ArrayList<String> array)
    {
        if (mArrayList!=null)
        {
            mArrayList.clear();
        }

        mArrayList.addAll(array);
        notifyDataSetChanged();
    }

}
