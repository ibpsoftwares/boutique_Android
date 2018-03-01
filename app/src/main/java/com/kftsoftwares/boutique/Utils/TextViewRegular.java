package com.kftsoftwares.boutique.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.kftsoftwares.boutique.R;

/**
 * Created by apple on 20/02/18.
 */

public class TextViewRegular extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TextView";

    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewRegular(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/whitney_medium.otf");
        setTypeface(tf);
    }
}
