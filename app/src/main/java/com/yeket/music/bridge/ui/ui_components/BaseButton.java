package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.yeket.music.R;

import static com.yeket.music.bridge.constants.Dimensions.BUTTON_ELEVATION;
import static com.yeket.music.bridge.constants.Dimensions.BUTTON_PADDING_BOTTOM;
import static com.yeket.music.bridge.constants.Dimensions.BUTTON_PADDING_LEFT;
import static com.yeket.music.bridge.constants.Dimensions.BUTTON_PADDING_RIGHT;
import static com.yeket.music.bridge.constants.Dimensions.BUTTON_PADDING_TOP;
import static com.yeket.music.bridge.constants.Dimensions.MEDIUM_TEXT;

public class BaseButton extends android.support.v7.widget.AppCompatButton {

    public BaseButton(Context context) {
        super(context);

        configureButton(context);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        configureButton(context);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        configureButton(context);
    }

    private void configureButton(Context context ) {

        // TODO: 22.08.17 додати стандартну хвильову анімацію андроїда при нажатті
        // TODO: 22.08.17 додати зміну елевейшна при нажатті

        Typeface customFont = FontCache.getTypeface(FontCache.COMFORTAA_BOLD, context);
        setTypeface(customFont);

        setTextSize(TypedValue.COMPLEX_UNIT_SP, MEDIUM_TEXT);

        setBackground(getResources().getDrawable(R.drawable.button_background));

        setHintTextColor(getResources().getColor(R.color.text_regular));

        setPadding(BUTTON_PADDING_LEFT, BUTTON_PADDING_TOP,
                BUTTON_PADDING_RIGHT, BUTTON_PADDING_BOTTOM);

        // TODO: 22.08.17 elevation for api < 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(BUTTON_ELEVATION);
        }

    }

}
