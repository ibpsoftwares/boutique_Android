package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kftsoftwares.boutique.Models.HistoryModel;
import com.kftsoftwares.boutique.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Symbol;

/**
 * Created by apple on 21/02/18.
 */

public class HistoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mCategoryName, mCategoryId;
    private ArrayList<HistoryModel> mHistoryModelArrayList;
    private SharedPreferences sharedPreferences;


    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModels) {

        mContext = context;
        mHistoryModelArrayList = historyModels;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mHistoryModelArrayList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_adapter, null);

        }
        TextView name = convertView.findViewById(R.id.name);

        TextView orderDate = convertView.findViewById(R.id.date);

        TextView price = convertView.findViewById(R.id.price);
        TextView orderStatus = convertView.findViewById(R.id.status);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView order_id = convertView.findViewById(R.id.order_id);
        ImageView product_image = convertView.findViewById(R.id.product_image);
        ImageView status_image = convertView.findViewById(R.id.status_image);


        String str = changeDateFormat(mHistoryModelArrayList.get(position).getCreated());

        orderDate.setText(str);


        price.setText("Amount: " + Html.fromHtml(sharedPreferences.getString(Symbol, "")) + " " + mHistoryModelArrayList.get(position).getPrice());

        name.setText(mHistoryModelArrayList.get(position).getTitle());
        order_id.setText("SKU:" + mHistoryModelArrayList.get(position).getOrderId());
        quantity.setText("Quantity: " + mHistoryModelArrayList.get(position).getQuantity());


        Glide.with(mContext).load(mHistoryModelArrayList.get(position).getImage())
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(product_image);
        if (mHistoryModelArrayList.get(position).getStatus() != null) {
            if (mHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("2")) {

                orderStatus.setText("Placed");
                status_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.delivered));
            } else if (mHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("3")) {

                orderStatus.setText("Shipped");
                status_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.shipped));


            } else if (mHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("4")) {

                orderStatus.setText("Delivered");
                status_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.delivered));


            } else if (mHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("5")) {
                orderStatus.setText("Cancelled");
                status_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.cancel));


            }

        }


        return convertView;
    }

    private String changeDateFormat(String date) {

        String formatedDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            formatedDate = output.format(input.parse(date));
            // parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

}
