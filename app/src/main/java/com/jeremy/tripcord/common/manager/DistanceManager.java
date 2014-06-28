package com.jeremy.tripcord.common.manager;

import android.location.Location;

import com.jeremy.tripcord.common.manager.exceptions.NoRecordingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asura1983 on 2014. 5. 19..
 */
public class DistanceManager {

    public static int INVALID_TRIP_SEQ = -1;

    private static DistanceManager instance = null;

    private List<Location> locationList;
    private double distance = 0;
    private int tripSeq = 0;

    private DistanceManager() {

        this.locationList = new ArrayList<Location>();
        setTripSeq(INVALID_TRIP_SEQ);
    }

    public static DistanceManager getInstance() {

        if (instance == null) {
            instance = new DistanceManager();
        }

        return instance;
    }

    public int getTripSeq() {
        return tripSeq;
    }

    public void setTripSeq(int tripSeq) {
        this.tripSeq = tripSeq;
    }

    public double getTotalDistance() {

        return distance;
    }

    public void addLocation(Location location) {

        locationList.add(location);

        calculate();
    }

    private void calculate() {

        if (locationList.size() > 1) {

            Location startLocation = locationList.get(locationList.size() - 2);
            Location endLocation = locationList.get(locationList.size() - 1);

            distance += startLocation.distanceTo(endLocation);
        }
    }

    public Location getLastLocation() throws NoRecordingException {

        if (locationList.size() > 0) {

            return locationList.get(locationList.size() - 1);
        } else {

            throw new NoRecordingException();
        }
    }

    public void reset() {

        locationList = new ArrayList<Location>();
        distance = 0;
        tripSeq = INVALID_TRIP_SEQ;
    }

}
