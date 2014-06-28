package com.jeremy.tripcord.common.database.domain;

import java.util.Date;

/**
 * Created by asura1983 on 2014. 6. 22..
 */
public class PhotoInfo {

    private int photoSeq;
    private int tripSeq;
    private String path;
    private double longitude;
    private double latitude;
    private Date created;

    public int getPhotoSeq() {
        return photoSeq;
    }

    public void setPhotoSeq(int photoSeq) {
        this.photoSeq = photoSeq;
    }

    public int getTripSeq() {
        return tripSeq;
    }

    public void setTripSeq(int tripSeq) {
        this.tripSeq = tripSeq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
