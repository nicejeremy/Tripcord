package com.jeremy.tripcord.common.database.domain;

import java.util.Date;

/**
 * Created by asura1983 on 2014. 6. 29..
 */
public class LocationInfo {

    private int locationSeq;
    private int tripSeq;
    private double latitude;
    private double longitude;
    private Date created;

    public int getLocationSeq() {
        return locationSeq;
    }

    public void setLocationSeq(int locationSeq) {
        this.locationSeq = locationSeq;
    }

    public int getTripSeq() {
        return tripSeq;
    }

    public void setTripSeq(int tripSeq) {
        this.tripSeq = tripSeq;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
