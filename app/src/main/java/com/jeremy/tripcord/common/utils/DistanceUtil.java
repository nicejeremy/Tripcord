package com.jeremy.tripcord.common.utils;

/**
 * Created by asura1983 on 2014. 7. 1..
 */
public class DistanceUtil {

    private static final int A_KILOMETER = 1000;

    public static String getDistance(int meter) {

        String distanceDescription = "";

        if ((meter / A_KILOMETER) == 0) {
            return meter + " m";
        } else {
            return (meter / A_KILOMETER) + "." + String.format("%03d", (meter % A_KILOMETER)) + "km";
        }
    }

}
