package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yeket.music.R;

import static com.yeket.music.bridge.constants.Dimensions.MEDIUM_TEXT;
import static com.yeket.music.bridge.constants.Dimensions.TEXT_PADDING_LEFT;

public class CustomTextView extends BaseRegularTextView {

    public CustomTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyCustomFont(context);
    }

    private void applyCustomFont( Context context ) {

        setTextSize(MEDIUM_TEXT);

        setTextColor(getResources().getColor(R.color.text_regular));

        setPadding(TEXT_PADDING_LEFT, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

}
