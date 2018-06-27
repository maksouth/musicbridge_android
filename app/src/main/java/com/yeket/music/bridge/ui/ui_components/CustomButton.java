package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.util.AttributeSet;

import com.yeket.music.R;

public class CustomButton extends BaseButton{
    
    public CustomButton(Context context) {
        super(context);

        configureButton(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        configureButton(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        configureButton(context);
    }

    private void configureButton(Context context ) {

        // TODO: 22.08.17 додати стандартну хвильову анімацію андроїда при нажатті
        // TODO: 22.08.17 додати зміну елевейшна при нажатті

        // TODO: 22.08.17 primary text dark
        setTextColor(getResources().getColor(R.color.text_regular));

    }
}
