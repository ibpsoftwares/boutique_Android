package com.kftsoftwares.boutique.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.activities.ProductList;
import com.kftsoftwares.boutique.activities.ShippingDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.Phone;
import static com.kftsoftwares.boutique.utils.Constants.User_Name;

/**
 * Created by apple on 21/02/18.
 */

public class PriceDetailAdapter extends BaseAdapter {

    private Context mContext;
    private JSONArray mJsonArray;
    private SharedPreferences sharedPreferences;
    private String mResponse;


    public PriceDetailAdapter(Context context , JSONArray jsonArray , String response) {

        mContext = context;
        mJsonArray = jsonArray;
        mResponse = response;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.price_detail_adapter,null);

        }

        Button button = convertView.findViewById(R.id.button);
        Button buttonEdit = convertView.findViewById(R.id.buttonEdit);
        TextView userName = convertView.findViewById(R.id.userName);
        TextView address = convertView.findViewById(R.id.address);
        TextView mobileNumber = convertView.findViewById(R.id.mobileNumber);
        try {
            JSONObject jsonObject = mJsonArray.getJSONObject(position);

            button.setText(jsonObject.getString("addressType"));

            String addressStr = jsonObject.getString("address")+"," + jsonObject.getString("locality")
                    + ","+  jsonObject.getString("city")+"," + jsonObject.getString("state")+"," +
                    jsonObject.getString("zip_code");

            address.setText(addressStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        userName.setText(sharedPreferences.getString(User_Name,""));
        mobileNumber.setText(sharedPreferences.getString(Phone,""));

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShippingDetails.class);
                intent.putExtra("jsonData",mResponse);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }
}
