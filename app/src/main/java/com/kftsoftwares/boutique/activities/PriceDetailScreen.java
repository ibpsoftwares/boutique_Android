

package com.kftsoftwares.boutique.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kftsoftwares.boutique.Adapter.PriceDetailAdapter;
import com.kftsoftwares.boutique.R;

public class PriceDetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_detail_screen);

        ListView listView = findViewById(R.id.listView);
        PriceDetailAdapter priceDetailAdapter = new PriceDetailAdapter(this);
     //   listView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 170*2));

        listView.setAdapter(priceDetailAdapter);
    }
}
