package com.kftsoftwares.boutique.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by apple on 20/02/18.
 */

public class TextViewBold extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TextView";

    public TextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewBold(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/whitney_bold.otf");
        setTypeface(tf);
    }
}
