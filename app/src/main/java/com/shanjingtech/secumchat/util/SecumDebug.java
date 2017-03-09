package com.shanjingtech.secumchat.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SecumDebug {
    private static final String DEBUG_MODE = "DEBUG_MODE";

    public static void enableDebugMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants
                .SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DEBUG_MODE, true);
        editor.commit();
    }

    public static void disableDebugMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants
                .SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DEBUG_MODE, false);
        editor.commit();
    }

    public static boolean isDebugMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants
                .SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DEBUG_MODE, false);
    }


}
