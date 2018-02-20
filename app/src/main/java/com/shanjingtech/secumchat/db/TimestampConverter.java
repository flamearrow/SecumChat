package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Convert String like "2017-11-29T03:39:13.249169Z" to integer of number of seconds
 */

public class TimestampConverter {
    private static final SimpleDateFormat SDF =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'");

    @TypeConverter
    public static long fromString(String s) {
        try {
            Date convertedDate = SDF.parse(s);
            return convertedDate.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    @TypeConverter
    public static String fromInt(long i) {
        return SDF.format(new Date(i));
    }
}
