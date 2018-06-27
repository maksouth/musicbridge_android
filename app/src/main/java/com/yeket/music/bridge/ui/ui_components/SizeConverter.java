package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class SizeConverter {

    public static int toPx(int dp, Context context){
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

}
