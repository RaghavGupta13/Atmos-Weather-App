package com.firstapp.atmos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Conversions {

    private static Date date;
    private static SimpleDateFormat simpleDateFormat;
    private static String pattern1 = "dd MMMM, yyyy";
    private static String pattern2 = "HH";

    public Conversions() {
    }

    public static String dateConversion(long json_date_time){
        date = new Date(json_date_time *1000);
        simpleDateFormat = new SimpleDateFormat(pattern1);
        return simpleDateFormat.format(date);
    }

    public static int timeConversionToHour(long json_date_time){
        date = new Date(json_date_time *1000);
        simpleDateFormat = new SimpleDateFormat(pattern2);
        return Integer.parseInt(simpleDateFormat.format(date));
    }
}
