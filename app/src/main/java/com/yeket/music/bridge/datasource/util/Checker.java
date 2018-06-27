package com.yeket.music.bridge.datasource.util;

import android.util.Log;

/**
 * Class has extract() methods
 * for different types
 * This method check object for null
 * if yes, then return value
 * if not null, object to String
 * and extract value of desired type.
 * The main purpose is prevent NPE in runtime
 * and replace null values with default values.
 * Later, error reporting will be added.
 */
// TODO: 03.01.18 add error reporting for null values
public abstract class Checker {

    public static final String TAG = Checker.class.getSimpleName();

    public static String extract(Object object, String defaultValue){
        return object == null
                ? defaultValue
                : object.toString();
    }

    public static Integer extract(Object object, Integer defaultValue){
        Integer result;

        try {
            result = object == null
                    ? defaultValue
                    : Integer.valueOf(object.toString());
        } catch (NumberFormatException nfe){
            Log.d(TAG, "While extract Integer " + nfe.getMessage(), nfe);
            result = defaultValue;
        }

        return result;
    }

    public static Long extract(Object object, Long defaultValue){

        Long result;

        try {
            result = object == null
                    ? defaultValue
                    : Long.valueOf(object.toString());
        } catch (NumberFormatException nfe){
            Log.d(TAG, "While extract Long " + nfe.getMessage(), nfe);
            result = defaultValue;
        }

        return result;
    }

    public static Boolean extract(Object object, Boolean defaultValue){
        return object == null
                ? defaultValue
                : Boolean.valueOf(object.toString());
    }

    public static Double extract(Object object, Double defaultValue){
        Double result;

        try {
            result = object == null
                    ? defaultValue
                    : Double.valueOf(object.toString());
        } catch (NumberFormatException nfe){
            Log.d(TAG, "While extract Double " + nfe.getMessage(), nfe);
            result = defaultValue;
        }

        return result;
    }

}
