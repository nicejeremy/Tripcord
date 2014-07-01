package com.jeremy.tripcord.common.utils;

/**
 * Created by asura1983 on 2014. 7. 1..
 */
public class TimeUtil {

    public static String getTimeDescription(int totalSecond) {

        if (totalSecond / 60 == 0) {
            return totalSecond + " s";
        } else if (totalSecond / 60 >= 1 && (totalSecond / 60) < 60) {
            int minute = totalSecond / 60;
            int second = totalSecond % 60;
            return minute + " m " + second + " s";
        } else if (totalSecond / (60 * 60) >= 1) {
            int hour = totalSecond / (60 * 60);
            int minute = (totalSecond % (60 * 60)) / 60;
            int second = (totalSecond % (60 * 60)) % 60;
            return hour + " h " + minute + " m " + second + "s";
        }

        return "";
    }

}
