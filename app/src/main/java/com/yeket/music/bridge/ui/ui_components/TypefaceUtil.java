package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

public class TypefaceUtil {

    private static final String DEFAULT_FONT_KEY = "SERIF";
    private static final String CUSTOM_FONT_NAME = "Comfortaa-Regular.ttf";

    /**
     * Using reflection to override default typeface
     */
    public static void overrideFont(Context context) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), CUSTOM_FONT_NAME);
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(DEFAULT_FONT_KEY);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("TypefaceUtil", "Cannot change typeface", e);
        }
    }
}
