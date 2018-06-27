package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class BaseRegularTextView extends android.support.v7.widget.AppCompatTextView {

    public BaseRegularTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public BaseRegularTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public BaseRegularTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyCustomFont(context);
    }

    private void applyCustomFont( Context context ) {

        Typeface customFont = FontCache.getTypeface(FontCache.COMFORTAA_REGULAR, context);
        setTypeface(customFont);

    }

}
