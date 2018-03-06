package com.kftsoftwares.boutique.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

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
                "fonts/whitney_book.otf");
        setTypeface(tf);
    }
}
