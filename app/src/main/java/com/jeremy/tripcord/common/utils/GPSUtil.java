package com.jeremy.tripcord.common.utils;

import android.location.Location;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by asura1983 on 2014. 5. 26..
 */
public class GPSUtil {

    public static void loc2Exif(String flNm, Location loc) {

        try {
            ExifInterface ef = new ExifInterface(flNm);
            ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE, dec2DMS(loc.getLatitude()));
            ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,dec2DMS(loc.getLongitude()));
            if (loc.getLatitude() > 0)
                ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            else
                ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            if (loc.getLongitude()>0)
                ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            else
                ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            ef.saveAttributes();
        } catch (IOException e) {}
    }

    private static String dec2DMS(double coord) {

        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543
        String sOut = Integer.toString((int)coord) + "/1,";   // 105/1,
        coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258
        sOut = sOut + Integer.toString((int)coord) + "/1,";   // 105/1,59/1,
        coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555
        sOut = sOut + Integer.toString((int)coord) + "/1000";   // 105/1,59/1,15555/1000
        return sOut;
    }

    public static Location exif2Loc(String flNm) {

        String sLat = "", sLatR = "", sLon = "", sLonR = "";
        try {
            ExifInterface ef = new ExifInterface(flNm);
            sLat  = ef.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            sLon  = ef.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            sLatR = ef.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            sLonR = ef.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        } catch (IOException e) {
            return null;
        }

        double lat = dms2Dbl(sLat);
        if (lat > 180.0) return null;
        double lon = dms2Dbl(sLon);
        if (lon > 180.0) return null;

        lat = sLatR.contains("S") ? -lat : lat;
        lon = sLonR.contains("W") ? -lon : lon;

        Location loc = new Location("exif");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }

    private static double dms2Dbl(String sDMS) {

        double dRV = 999.0;
        try {
            String[] DMSs = sDMS.split(",", 3);
            String s[] = DMSs[0].split("/", 2);
            dRV = (new Double(s[0])/new Double(s[1]));
            s = DMSs[1].split("/", 2);
            dRV += ((new Double(s[0])/new Double(s[1]))/60);
            s = DMSs[2].split("/", 2);
            dRV += ((new Double(s[0])/new Double(s[1]))/3600);
        } catch (Exception e) {}
        return dRV;
    }

}
