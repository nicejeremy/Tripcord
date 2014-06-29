package com.jeremy.tripcord.record.model;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.database.domain.TripInfo;

/**
 * Created by asura1983 on 2014. 6. 29..
 */
public class RecordModel {

    public static int createTripInfo(Context context) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        int createdTripSeq = databaseManager.insertTripInfo();

        return createdTripSeq;
    }

    public static int deleteCurrentTripInfo(Context context, int tripSeq) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        int result = databaseManager.deleteCurrentTripInfo(tripSeq);

        return result;
    }

    public static int updateTripSnapShot(Context context, int tripSeq, byte[] snapShot) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        int result = databaseManager.updateTripSnapShot(tripSeq, snapShot);

        return result;
    }

    public static int insertLocation(Context context, int tripSeq, LatLng latLng) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        int result = databaseManager.insertLocation(tripSeq, latLng);

        return result;
    }

    public static int updateTripInfoAsFinish(Context context, int tripSeq, double distance, int duringTime, String arrival, String destination) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        int result = databaseManager.updateTripInfoAsFinish(tripSeq, distance, duringTime, arrival, destination);

        return result;
    }

    public static TripInfo loadTripInfo(Context context, int tripSeq) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        TripInfo tripInfo = databaseManager.selectTripInfo(tripSeq);
        tripInfo.setPhotoInfoList(databaseManager.selectTripPhotos(tripSeq));

        return tripInfo;
    }

    public static int updateTripDetailInfo(Context context, int tripSeq, String title, String description, String feel, String transportation, String weather) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        int result = databaseManager.updateTripDetailInfo(tripSeq, title, description, feel, transportation, weather);

        return result;
    }
}
