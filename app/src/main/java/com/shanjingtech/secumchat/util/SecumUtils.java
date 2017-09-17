package com.shanjingtech.secumchat.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SecumUtils {
    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static String getCurrentTime() {
        return df.format(Calendar.getInstance().getTime());
    }
}
