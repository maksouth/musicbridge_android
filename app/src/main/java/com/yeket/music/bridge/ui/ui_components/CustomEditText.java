package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.yeket.music.R;

import static com.yeket.music.bridge.constants.Dimensions.MEDIUM_TEXT;
import static com.yeket.music.bridge.constants.Dimensions.TEXT_PADDING_LEFT;
import static com.yeket.music.bridge.constants.Dimensions.TEXT_PADDING_RIGHT;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    public CustomEditText(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context ) {

        Typeface customFont = FontCache.getTypeface(FontCache.COMFORTAA_BOLD, context);
        setTypeface(customFont);

        setTextSize(MEDIUM_TEXT);

        setTextColor(getResources().getColor(R.color.text_dark));

        setSingleLine();

        setPadding(TEXT_PADDING_LEFT, getPaddingTop(), TEXT_PADDING_RIGHT, getPaddingBottom());

    }

}
