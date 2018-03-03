package com.kftsoftwares.boutique.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kftsoftwares.boutique.Adapter.ViewPagerAdapterZoomActivity;
import com.kftsoftwares.boutique.R;

import java.util.ArrayList;

public class ZoomActivity extends AppCompatActivity {
    String mImageData;
    ArrayList<String> mObject;
    int mPosition =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

       final ViewPager viewPager = findViewById(R.id.viewPager);
        Bundle b = getIntent().getExtras();
        if (b!=null)
        {
            if (b.getString("image")!=null)
            {
                mImageData = b.getString("image");
            }
            if (b.getStringArrayList("allImages")!=null)
            {
                mObject = b.getStringArrayList("allImages");
            }
            if (b.getInt("position")!=0)
            {
                mPosition =b.getInt("position");
            }
        }

        ViewPagerAdapterZoomActivity  viewPagerAdapterZoomActivity = new ViewPagerAdapterZoomActivity(this,mObject);

        viewPager.setAdapter(viewPagerAdapterZoomActivity);
        ImageView backButton = findViewById(R.id.backButton);
        viewPager.setCurrentItem(mPosition);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
