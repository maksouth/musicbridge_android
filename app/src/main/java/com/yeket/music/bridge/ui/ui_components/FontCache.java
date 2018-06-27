package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

class FontCache {

    public static final String COMFORTAA_BOLD = "Comfortaa-Bold.ttf";
    public static final String COMFORTAA_LIGHT = "Comfortaa-Light.ttf";
    public static final String COMFORTAA_REGULAR = "Comfortaa-Regular.ttf";

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}