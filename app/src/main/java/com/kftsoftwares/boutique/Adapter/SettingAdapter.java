package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kftsoftwares.boutique.R;

/**
 * Created by apple on 08/05/18.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {

    private Context mContext;
    private int images[] = {R.mipmap.ic_help_white, R.mipmap.ic_profile_white,
            R.mipmap.ic_password_white, R.mipmap.ic_history_white, R.mipmap.ic_signout_white};
    private String name[] = { "Help","Profile","Change Password","History","Signout"};

    private int colors[] = { R.color.help_background,
            R.color.profile_background,
            R.color.changePassword_background
            , R.color.history_background,
            R.color.signOut_background};

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_grid_layout, parent, false);
        return new SettingAdapter.MyViewHolder(itemView);
    }

    public SettingAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String[] allColors = mContext.getResources().getStringArray(R.array.colors);

        holder.imageView.setImageResource(images[position]);
        holder.relativeLayout.setBackgroundColor(Color.parseColor(allColors[position]));
        holder.textView.setText(name[position]);
       // holder.cardview.setCardBackgroundColor(colors[position]);

    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout relativeLayout;
        TextView textView;
        CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
