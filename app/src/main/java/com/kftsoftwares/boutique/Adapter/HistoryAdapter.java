package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kftsoftwares.boutique.Models.HistoryModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.ProductList;

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
    private ArrayList<String> mCategoryName,mCategoryId;
    private ArrayList<HistoryModel> mHistoryModelArrayList;
    private SharedPreferences sharedPreferences;


    public HistoryAdapter(Context context , ArrayList<HistoryModel> historyModels) {

        mContext = context;
        mHistoryModelArrayList = historyModels;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
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

        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_adapter,null);

        }
        TextView orderId = convertView.findViewById(R.id.orderId);

        TextView orderDate = convertView.findViewById(R.id.orderDate);

        TextView amount = convertView.findViewById(R.id.amount);


        orderDate.setText(changeDateFormat(mHistoryModelArrayList.get(position).getCreated()));

        amount.setText(Html.fromHtml(sharedPreferences.getString(Symbol,""))+" "+mHistoryModelArrayList.get(position).getAmount());

        orderId.setText(mHistoryModelArrayList.get(position).getOrder_detail_id());



        return convertView;
    }

    private String changeDateFormat(String date) {

        String formatedDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat serverOutPut = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            formatedDate = output.format(input.parse(date));
           String mServerDate = serverOutPut.format(input.parse(date));
            // parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

}
