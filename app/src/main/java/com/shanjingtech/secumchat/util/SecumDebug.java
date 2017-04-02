package com.shanjingtech.secumchat.util;

import android.content.SharedPreferences;

public class SecumDebug {
    public static final String DEBUG_MODE = "DEBUG_MODE";
    public static final String CURRENT_DEBUG_USER = "CURRENT_DEBUG_USER";
    public static final String USER_11 = "USER_11";
    public static final String TOKEN_11 = "vUXSNoPy3XLb3oh51zrhrYqQoDaVGd";
    public static final String USER_22 = "USER_22";
    public static final String TOKEN_22 = "zhvG2zIf4xXFzzFfTJjnfOycXTjBZn";

    public static void enableDebugMode(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DEBUG_MODE, true);
        editor.commit();
    }

    public static void disableDebugMode(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DEBUG_MODE, false);
        editor.commit();
    }

    public static boolean isDebugMode(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(DEBUG_MODE, false);
    }

    public static void setDebugUser(SharedPreferences sharedPreferences, String user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_DEBUG_USER, user);
        editor.commit();
    }

    public static String getCurrentDebugUser(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(CURRENT_DEBUG_USER, "");
    }

}
